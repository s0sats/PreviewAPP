package com.namoadigital.prj001.service

import android.content.Intent
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao
import com.namoadigital.prj001.extensions.serial.executeDbTransaction
import com.namoadigital.prj001.extensions.serial.refreshStructureHeader
import com.namoadigital.prj001.extensions.serial.setStructuresPK
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Structure
import com.namoadigital.prj001.model.TMDProductSerialStructureObj
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure_Env
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure_Rec
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure_Worker_Rec
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Structure
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.MDProductSerialSql018
import com.namoadigital.prj001.sql.MDProductSerialSql019
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WS_Product_Serial_Structure :
    BaseWsIntentService("WS_Product_Serial_Structure", IntentServiceMode.DOWNLOAD_DATA()) {

    private val hmAux_Trans by lazy {

        val transList: MutableList<String> = ArrayList()
        transList.add("msg_no_data_returned")
        transList.add("generic_sending_data_msg")
        transList.add("generic_receiving_data_msg")
        transList.add("generic_processing_data")
        transList.add("generic_process_finalized_msg")
        transList.add("msg_error_on_serial_structure")
        transList.add("msg_error_serial_not_found")

        mResource_Code = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModule_Code,
            mResource_Name
        )

        ToolBox_Inf.setLanguage(
            applicationContext,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            transList
        )
    }

    //
    private val serialDao: MD_Product_SerialDao by lazy {
        MD_Product_SerialDao(
            applicationContext,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(applicationContext)),
            ConstantBaseApp.DB_VERSION_CUSTOM
        )
    }

    //
    private val tpDeviceDao: MD_Product_Serial_Tp_DeviceDao by lazy {
        MD_Product_Serial_Tp_DeviceDao(
            applicationContext,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(applicationContext)),
            Constant.DB_VERSION_CUSTOM
        )
    }

    private val productSerialVGDao: MDProductSerialVGDao by lazy {
        MDProductSerialVGDao(applicationContext)
    }

    private val mModule_Code: String = Constant.APP_MODULE
    private var mResource_Code = "0"
    private val mResource_Name = "ws_product_serial_structure"
    private val gson = GsonBuilder().serializeNulls().create()
    private var amountTotal = -1
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        val bundle = intent!!.extras
        //
        try {
//            cancelTicketDownloadWorker(applicationContext)
            val customerCode = bundle!!.getLong(MD_Product_SerialDao.CUSTOMER_CODE, -1)
            val productCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE, -1)
            val serialCode = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE, -1)
            val scnItemCheck = bundle.getInt(MD_Product_SerialDao.SCN_ITEM_CHECK, -1)
            amountTotal = bundle.getInt(AMOUNT_TOTAL, -1)
            processSerialStructure(customerCode, productCode, serialCode, scnItemCheck)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            if (ToolBox_Con.isHttpError(e)) {
                ToolBox_Inf.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_ERROR_HTTP,
                    sb.toString(),
                    "",
                    "0"
                )
            } else {
                ToolBox_Inf.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_ERROR_1,
                    sb.toString(),
                    "",
                    "0"
                )
            }
        } finally {
//            scheduleDownloadTicket(applicationContext)
            WBR_Product_Serial_Structure.completeWakefulIntent(intent)
        }
    }


    private fun processSerialStructure(
        customerCode: Long,
        productCode: Long,
        serialCode: Long,
        scnItemCheck: Int,
    ) {
        val getSyncStructureRemains = amountTotal - getSyncStructureRemains()
        //
        val env = T_MD_Product_Serial_Structure()
        //
        env.setZipMode(0)
        val timeout:Int? = if (customerCode == -1L) {
            env.setSearch(
                getSerialStructureOutdated()
            )
            env.setZipMode(1)
            ConstantBaseApp.TIMEOUT_FOR_SYNC_FULL
        } else {
            env.getSearch().add(
                T_MD_Product_Serial_Structure_Env(
                    customerCode,
                    productCode,
                    serialCode,
                    scnItemCheck
                )
            )
            null
        }
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_sending_data_msg"],
            "",
            "0"
        )
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT
        //

        val resultado = ToolBox_Con.connWebService(
            Constant.WS_PRODUCT_SERIAL_STRUCTURE_SEARCH,
            gson.toJson(env),
            timeout
        )
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_receiving_data_msg"],
            "",
            "0"
        )
        //
        val structures =  mutableListOf<MD_Product_Serial_Structure>()
        //
        if(customerCode == -1L){
            //
            val rec = gson.fromJson(
                resultado,
                T_MD_Product_Serial_Structure_Worker_Rec::class.java
            )
            //
            if (rec.url == null) {
                ToolBox.registerException(javaClass.name, Exception("Url preenchida como nulo"))
                return
            }
            //
            ToolBox_Inf.downloadZip(rec.url, Constant.ZIP_NAME_FULL)
            //
            ToolBox_Inf.unpackZip("", Constant.ZIP_NAME)
            //
            val listoffilesV2 =
                ToolBox_Inf.getListOfFiles_v2("product_serial_structure")
            ////
            val structureObj = gson.fromJson<TMDProductSerialStructureObj>(
                ToolBox.jsonFromOracle(
                    ToolBox_Inf.getContents(listoffilesV2[0])
                ),
                object : TypeToken<TMDProductSerialStructureObj>() {}.type
            )
            //
            ToolBox_Inf.deleteAllFOD(Constant.ZIP_PATH)
            //
            if (!structureObj.structure.isNullOrEmpty()) {
                structures.addAll(structureObj.structure)
            }
        } else {
            val rec = gson.fromJson(
                resultado,
                T_MD_Product_Serial_Structure_Rec::class.java
            )
            //
            if (!ToolBox_Inf.processWSCheckValidation(
                    applicationContext,
                    rec.validation,
                    rec.error_msg,
                    rec.link_url,
                    1,
                    1
                )
                ||
                !ToolBox_Inf.processoOthersError(
                    applicationContext,
                    resources.getString(R.string.generic_error_lbl),
                    rec.error_msg
                )
            ) {
                return
            }
            rec.structure?.let{
                structures.addAll(it)
            }?:run{
                return
            }
        }
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_processing_data"] + getProgressInfo(getSyncStructureRemains,amountTotal),
            "",
            "0"
        )
        //
        if (structures.isNotEmpty()) {
            processSerialStructureReturn(structures, customerCode)
        } else {
            ToolBox.sendBCStatus(
                applicationContext,
                "ERROR_1",
                hmAux_Trans["msg_no_data_returned"],
                "",
                "0"
            )
        }
    }

    private fun getProgressInfo(getSyncStructureRemains:Int, amountTotal: Int): String = if(amountTotal > 0){
        " ${getSyncStructureRemains}/$amountTotal"
    }else{
        ""
    }

    private fun getSyncStructureRemains(): Int {

        val query = serialDao.query(
            MDProductSerialSql018(
                ToolBox_Con.getPreference_Customer_Code(applicationContext)
            ).toSqlQuery()
        )

        return query.size
    }

    private fun getSerialStructureOutdated(): java.util.ArrayList<T_MD_Product_Serial_Structure_Env> {
        val search = mutableListOf<T_MD_Product_Serial_Structure_Env>()
        val query = serialDao.query(
            MDProductSerialSql019(
                ToolBox_Con.getPreference_Customer_Code(applicationContext)
            ).toSqlQuery()
        )
        query.forEach {
            search.add(
                T_MD_Product_Serial_Structure_Env(
                    it.customer_code,
                    it.product_code,
                    it.serial_code,
                    it.scn_item_check
                )
            )
        }
        return search as java.util.ArrayList<T_MD_Product_Serial_Structure_Env>
    }


//    private fun getSerialStructureOutdated(serialSyncList: String): java.util.ArrayList<T_MD_Product_Serial_Structure_Env> {
//        val search = mutableListOf<T_MD_Product_Serial_Structure_Env>()
//        val pkList = serialSyncList.split("|")
//        pkList.forEach {
//            search.add(
//                T_MD_Product_Serial_Structure_Env(
//                    it[0].code.toLong(),
//                    it[1].code.toLong(),
//                    it[2].code.toLong(),
//                    0
//                )
//            )
//        }
//        return search as java.util.ArrayList<T_MD_Product_Serial_Structure_Env>
//    }

    private fun processSerialStructureReturn(
        structures: List<MD_Product_Serial_Structure>,
        customerCode: Long
    ) {
        var syncStructureRemains = amountTotal -  getSyncStructureRemains()
        /**
         * REGRA
         * customerCode == -1 define se eh sync ou atualizadcao especifica de estrutura.
         */
        if (customerCode == (-1).toLong()) {
            //
            var dbResult: Boolean = true
            for (structure in structures) {
                //
                val serial = serialDao.getByString(
                    MD_Product_Serial_Sql_009(
                        structure.customer_code,
                        structure.product_code,
                        structure.serial_code
                    ).toSqlQuery()
                )
                serial.refreshStructureHeader(structure)
                //
                if (serial != null) {
                    //
                    serial.setStructuresPK(structure)
                    //
                    dbResult = serial.executeDbTransaction(
                        applicationContext,
                        serialDao,
                        tpDeviceDao,
                        productSerialVGDao,
                        structure
                    )
                }
                //
                if(dbResult) {
                    syncStructureRemains++
                }
                //
                if(syncStructureRemains  % 20 == 0){
                    ToolBox.sendBCStatus(
                        applicationContext,
                        "STATUS",
                        hmAux_Trans["generic_processing_data"] + getProgressInfo(syncStructureRemains, amountTotal),
                        "",
                        "0"
                    )
                }
            }
            //
            if (dbResult) {
                val msg = if(amountTotal > 0 && getSyncStructureRemains() > 0){
                    hmAux_Trans["generic_processing_data"] + getProgressInfo(syncStructureRemains, amountTotal)
                }else{
                    hmAux_Trans["generic_process_finalized_msg"]
                }
                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    msg,
                    "",
                    "0"
                )
            } else {
                ToolBox.sendBCStatus(
                    applicationContext,
                    "ERROR_1",
                    hmAux_Trans["msg_error_on_serial_structure"],
                    "",
                    "0"
                )
            }

        } else {
            val serialStructure = structures[0]
            //
            val serialUpdateInfo = serialDao.getByString(
                MD_Product_Serial_Sql_009(
                    serialStructure.customer_code,
                    serialStructure.product_code,
                    serialStructure.serial_code
                ).toSqlQuery()
            )
            //
            serialUpdateInfo.refreshStructureHeader(serialStructure)
            if (serialUpdateInfo != null) {
                serialUpdateInfo.setStructuresPK(serialStructure)
                val result = serialUpdateInfo.executeDbTransaction(
                    applicationContext,
                    serialDao,
                    tpDeviceDao,
                    productSerialVGDao,
                    serialStructure
                )
                if(result){
                    val result = gson.toJson(serialUpdateInfo)
                    ToolBox.sendBCStatus(
                        applicationContext,
                        "CLOSE_ACT",
                        hmAux_Trans["generic_process_finalized_msg"],
                        result,
                        "0"
                    )
                }else{
                    ToolBox.sendBCStatus(
                        applicationContext,
                        "ERROR_1",
                        hmAux_Trans["msg_error_on_serial_structure"],
                        "",
                        "0"
                    )
                }
            } else {
                ToolBox.sendBCStatus(
                    applicationContext,
                    "ERROR_1",
                    hmAux_Trans["msg_error_serial_not_found"],
                    "",
                    "0"
                )
            }
        }

    }

    private fun executeDbTransaction(
        serial: MD_Product_Serial,
        structure: MD_Product_Serial_Structure,
        dbResult: Boolean
    ): Boolean {
        var dbResult1 = dbResult
        DatabaseTransactionManager(applicationContext).executeTransactionDaoObjReturn { database ->
            serialDao.addUpdate(serial)
            var daoObjReturn = serialDao.removeFullStructure(serial)

            if (!daoObjReturn.hasError()) {
                daoObjReturn = tpDeviceDao.addUpdate(
                    structure.device_tp,
                    false,
                    database
                )
            }

            if (!daoObjReturn.hasError()) {
                daoObjReturn = productSerialVGDao.addUpdate(
                    structure.verificationGroup,
                    false,
                    database
                )
            }

            daoObjReturn
        }.watchStatus(
            success = {
                dbResult1 = true
            },
            failed = {
                dbResult1 = false
            }
        )
        return dbResult1
    }

    companion object {
        const val SERIAL_SYNC_LIST_BUNDLE = "SERIAL_SYNC_LIST_BUNDLE"
        const val AMOUNT_TOTAL = "AMOUNT_TOTAL"
    }
}