package com.namoadigital.prj001.worker.big_file

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao
import com.namoadigital.prj001.extensions.getMd5
import com.namoadigital.prj001.extensions.serial.executeDbTransaction
import com.namoadigital.prj001.extensions.serial.refreshStructureHeader
import com.namoadigital.prj001.extensions.serial.setStructuresPK
import com.namoadigital.prj001.extensions.ticket.processTicketAndSerialConciliation
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Structure
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.TMDProductSerialStructureObj
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.service.trip.WsUserPosition.Companion.UPDATE_CLOUD
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.util.singleton.ticket.TicketDownloadRestriction
import com.namoadigital.prj001.worker.BaseRemoteBigFileUseCase
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

class DownloadBigFileUseCase(
    context: Context,
) : BaseRemoteBigFileUseCase(context) {
    private val mutex = Mutex()
    private val gson = GsonBuilder().serializeNulls().create()
    private var checkSum: Boolean = false
    //
    private val serialDao: MD_Product_SerialDao by lazy {
        MD_Product_SerialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            ConstantBaseApp.DB_VERSION_CUSTOM
        )
    }

    private val ticketDao: TK_TicketDao by lazy {
        TK_TicketDao(
            context,
        )
    }

    //
    private val tpDeviceDao: MD_Product_Serial_Tp_DeviceDao by lazy {
        MD_Product_Serial_Tp_DeviceDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val productSerialVGDao: MDProductSerialVGDao by lazy {
        MDProductSerialVGDao(context)
    }

    override suspend fun invoke(inputData: DataInput): DataOutput {

        val fileType = inputData.bigFile.fileType
        val fileStatus = inputData.bigFile.fileStatus
        val fileUrl = inputData.bigFile.fileUrl
        val fileMD5 = inputData.bigFile.fileMd5
        //
        var fileStatusResult: String = BigFileStatus.NO_VALUE.name
        //
        debugBigFile("DownloadBigFileUseCase", inputData.bigFile)
        mutex.withLock {
            return try {

                val prefix = when(fileType){
                    BigFilePreferenceManager.FILE_TYPE_SERIAL_STRUCTURE -> "product_serial_structure"
                    BigFilePreferenceManager.FILE_TYPE_TICKET -> "ticket"
                    else -> null
                }

                if (checkUserLoginOrWorkerStopped()) {
                    debugBigFile("checkUserLoginOrWorkerStopped", null)
                    return formatErrorMsg("Worker Finalizado por Sessao")
                } else {

                    if(prefix != null
                        && fileMD5 != null){
                        val localPath = getLocalPath(fileType)

                        executeDownload(fileUrl, localPath)

                        if (localPath != null
                            && File(localPath).exists()
                            && File(localPath).length() > 0L
                            && checkMd5sum(File(localPath), fileMD5)) {
                            ToolBox_Inf.unpackZip(
                                ConstantBaseApp.BIG_FILE_JSON_PATH + "/",
                                getLocalZipName(fileType)
                            )
                            //
                            val syncFiles =
                                ToolBox_Inf.getListOfFiles_v5(
                                    ConstantBaseApp.BIG_FILE_JSON_PATH,
                                    prefix
                                )
                            //
                            syncFiles?.let {

                                when (fileType) {
                                    BigFilePreferenceManager.FILE_TYPE_SERIAL_STRUCTURE -> {
                                        fileStatusResult = executeSyncFromDownloadFile(fileType) {
                                            processStructureDownload(syncFiles)
                                        }
                                    }

                                    BigFilePreferenceManager.FILE_TYPE_TICKET -> {
                                        fileStatusResult = executeSyncFromDownloadFile(fileType) {
                                            processTicketDownload(syncFiles)
                                        }
                                    }
                                }
                                //
                                ToolBox_Inf.deleteDownloadFile(syncFiles[0].path)
                                //
                            }
                        } else{
                            fileStatusResult = BigFileStatus.NOT_FOUND.name
                        }
                        //
                        ToolBox_Inf
                            .deleteDownloadFile(
                                ConstantBaseApp.BIG_FILE_JSON_PATH + "/" + getLocalZipName(fileType)
                            )
                    }
                    debugBigFile("fileStatusResult: $fileStatusResult", null)
                    when(fileStatusResult){
                        BigFileStatus.PENDING.name ->{
                            context.sendBCStatus(WsTypeStatus.FCMStatus(UPDATE_CLOUD))
                            //
                            val bigFile = setBigFile(fileType, BigFileStatus.PENDING.name)
                            DataOutput.Success(bigFile)
                        }
                        BigFileStatus.NOT_FOUND.name ->{
                            val bigFile = setBigFile(fileType, BigFileStatus.PENDING.name)
                            DataOutput.Success(bigFile)
                        }
                        else->{
                            setBigFile(fileType, BigFileStatus.PROCESS.name)
                            formatErrorMsg("Erro no processamento fileStatusResult: $fileStatusResult")
                        }
                    }
                }
            } catch (exception: Exception) {
                debugBigFile("exception: $exception", null)
                if(ToolBox_Con.isHttpError(exception)){
                    setBigFile(fileType, BigFileStatus.DOWNLOAD.name)
                    ToolBox_Inf.registerException(javaClass.name, exception)
                }else {
                    setBigFile(fileType, BigFileStatus.PROCESS.name)
                    ToolBox.registerException(javaClass.name, exception)
                }
                formatErrorMsg("${exception.message}")
            }
        }

    }
    @Throws(Exception::class)
    private fun executeDownload(fileUrl: String?, localPath: String?) {
        try {
            ToolBox_Inf.downloadZip(fileUrl, localPath)
        }catch (e: Exception){
            throw java.lang.Exception(Constant.WS_EXCEPTION_HTTP_STATUS_ERROR)
        }
    }

    private fun debugWithNotification(msg: String) {
        ToolBox.registerException("BIG_FILE", Exception(msg))
    }

    private fun getLocalZipName(prefix: String): String?{
        return when (prefix) {
            BigFilePreferenceManager.FILE_TYPE_SERIAL_STRUCTURE -> ConstantBaseApp.ZIP_STRUCTURE_NAME
            BigFilePreferenceManager.FILE_TYPE_TICKET -> ConstantBaseApp.ZIP_TICKET_NAME
            else -> null
        }

    }

    private fun getLocalPath(prefix: String): String?{
        return when (prefix) {
            BigFilePreferenceManager.FILE_TYPE_SERIAL_STRUCTURE -> ConstantBaseApp.ZIP_STRUCTURE_NAME_FULL
            BigFilePreferenceManager.FILE_TYPE_TICKET -> ConstantBaseApp.ZIP_TICKET_NAME_FULL
            else -> null
        }
    }

    private fun checkMd5sum(
        downloadedFile: File,
        checksum: String
    ): Boolean {

        val md5 = downloadedFile.getMd5()
        checkSum = md5.equals(checksum, true)
        return  md5.equals(checksum, true)
    }

    private fun processTicketDownload(ticketFile: Array<File>): Boolean {

        val ticketsResp = gson.fromJson(
            ToolBox.jsonFromOracle(
                ToolBox_Inf.getContents(ticketFile[0])
            ),
            T_TK_Ticket_Download_Rec::class.java
        )

        if (ticketsResp != null && !ticketsResp.ticket.isNullOrEmpty()) {
            if (TicketDownloadRestriction.isTicketDownloadRestrictionInitialized()) {
                ticketsResp.ticket.filter {
                    !(it.ticket_prefix.toString() == TicketDownloadRestriction.ticketPrefixRestriction
                            && it.ticket_code.toString() == TicketDownloadRestriction.ticketCodeRestriction)
                }
            }
            if (!processTicketReturn(ticketsResp.ticket)) {
                return false
            }
        }
        return true
    }

    private fun processTicketReturn(ticket: java.util.ArrayList<TK_Ticket>): Boolean {
        return ticketDao.processTicketAndSerialConciliation(
            context,
            ticket,
            TkTicketCacheDao(context),
            serialDao
        )
    }

    fun executeSyncFromDownloadFile(
        fileType: String,
        checkDownloadSuccess: () -> Boolean
    ): String{
        val bigFilePreferenceManager =
            BigFilePreferenceManager(context, fileType)
        val bigFile =
            bigFilePreferenceManager.getBigFile()

        if(checkDownloadSuccess()){
            bigFile.fileMd5 = null
            bigFile.fileUrl = null
            bigFile.fileStatus = BigFileStatus.PENDING.name
        }else{
            bigFile.fileStatus = BigFileStatus.PROCESS.name
        }
        bigFilePreferenceManager.saveBigFile(bigFile)

        return bigFile.fileStatus ?: BigFileStatus.NO_VALUE.name

    }

    private fun setBigFile(fileType: String?, status:String): BigFile {
        val bigFilePreferenceManager =
            BigFilePreferenceManager(context, fileType!!)
        val bigFile =
            bigFilePreferenceManager.getBigFile()
        bigFile.fileStatus = status
        bigFilePreferenceManager.saveBigFile(bigFile)
        return bigFile
    }
    @Throws(Exception::class)
    private fun processStructureDownload(listoffilesV2: Array<File>): Boolean {
        val serialStructures = gson.fromJson<TMDProductSerialStructureObj>(
            ToolBox.jsonFromOracle(
                ToolBox_Inf.getContents(listoffilesV2[0])
            ),
            object : TypeToken<TMDProductSerialStructureObj>() {}.type
        )
        if (serialStructures != null && !serialStructures.structure.isNullOrEmpty()) {
            if (!processSerialStructureReturn(serialStructures.structure)) {
                return false

            }
        }
        return true
    }
    @Throws(Exception::class)
    private fun processSerialStructureReturn(structures: List<MD_Product_Serial_Structure>): Boolean {
        var dbResult = true
        for (structure in structures) {
            val serial = serialDao.getSerial(
                structure.customer_code,
                structure.product_code,
                structure.serial_code,
            )
            if (serial != null){
                if(structure.scn_item_check != null
                    && serial.scn_item_check != null
                    && structure.scn_item_check > serial.scn_item_check
                    ) {
                    //
                    serial.refreshStructureHeader(structure)
                    //
                    serial.setStructuresPK(structure)
                    //
                    dbResult = serial.executeDbTransaction(
                        context,
                        serialDao,
                        tpDeviceDao,
                        productSerialVGDao,
                        structure
                    )
                }else{
                    serial.syncStructure = 0
                    serial.syncBigFile = 0
                    serialDao.addUpdate(serial)
                }
            }
        }
        return dbResult
    }

    private fun serialUpdateInfo(
        serial: MD_Product_Serial,
        serialStructure: MD_Product_Serial_Structure
    ){
        //
        serial.has_item_check  = serialStructure.has_item_check
        serial.scn_item_check  = serialStructure.scn_item_check
        serial.measure_tp_code  = serialStructure.measure_tp_code
        serial.last_measure_value  = serialStructure.last_measure_value
        serial.last_measure_date  = serialStructure.last_measure_date
        serial.last_cycle_value  = serialStructure.last_cycle_value
        serial.last_cycle_date  = serialStructure.last_cycle_date
        serial.log_date  = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        serial.horimeter  = serialStructure.horimeter
        serial.horimeter_date  = serialStructure.horimeter_date
        serial.horimeter_supplier_uid  = serialStructure.horimeter_supplier_uid
        serial.horimeter_supplier_desc  = serialStructure.horimeter_supplier_desc
        serial.measure_block_input_time  = serialStructure.measure_block_input_time
        serial.measure_alert_input_time  = serialStructure.measure_alert_input_time
        serial.unavailability_reason_option  = serialStructure.unavailability_reason_option
        serial.syncStructure = 0
        serial.syncBigFile = 0
        //

    }

    private fun checkUserLoginOrWorkerStopped():
            Boolean = (ToolBox_Con.getPreference_Customer_Code(context) == -1L
            || ToolBox_Con.getPreference_User_Code(context).isBlank())

}