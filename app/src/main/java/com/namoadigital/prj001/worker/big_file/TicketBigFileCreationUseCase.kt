package com.namoadigital.prj001.worker.big_file

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.extensions.dao.ticket.getBigFileSyncList
import com.namoadigital.prj001.extensions.dao.ticket_cache.getSyncBigFile
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env
import com.namoadigital.prj001.model.big_file.create_file.BigFileRec
import com.namoadigital.prj001.model.big_file.create_file.BigFileTicketCreationEnv
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_TICKET
import com.namoadigital.prj001.util.singleton.ticket.TicketDownloadRestriction
import com.namoadigital.prj001.worker.BaseRemoteBigFileUseCase
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus

class TicketBigFileCreationUseCase (
    context: Context,
) : BaseRemoteBigFileUseCase(context) {

    private val gson: Gson = GsonBuilder().serializeNulls().create()
    //
    val tkTicketCachedao by lazy {
        TkTicketCacheDao(context)
    }
    //
    val ticketDao by lazy {
        TK_TicketDao(context)
    }
    @Throws(Exception::class)
    override suspend fun invoke(inputData: DataInput): DataOutput {
        val fileStatus = inputData.bigFile.fileStatus
        debugBigFile("TicketBigFileCreationUseCase", inputData.bigFile)
        //
        if (fileStatus != BigFileStatus.PENDING.name) {
            return formatErrorMsg("Worker Finalizado por fileStatus: $fileStatus")
        }
        try {
            //
            val syncList = getTicketCacheForBigFile(context)
            syncList.addAll(
                ticketDao.getBigFileSyncList(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    ToolBox_Con.getPreference_Site_Code(context).toString()
                )
            )
            val ticketList = if (TicketDownloadRestriction.isTicketDownloadRestrictionInitialized()) {
                syncList.filter {
                    !(it.ticket_prefix == TicketDownloadRestriction.ticketPrefixRestriction
                            && it.ticket_code == TicketDownloadRestriction.ticketCodeRestriction)
                }
            } else {
                syncList
            }.take(500)
            //
            if (ticketList.isNotEmpty()) {
                val env = BigFileTicketCreationEnv(
                    FILE_TYPE_TICKET,
                    0,
                    ticketList
                )
                env.app_code = Constant.PRJ001_CODE
                env.app_version = Constant.PRJ001_VERSION
                env.session_app = ToolBox_Con.getPreference_Session_App(context)
                env.app_type = Constant.PKG_APP_TYPE_DEFAULT
                //

                val response = ToolBox_Con.connWebService(
                        Constant.WS_TICKET_DOWNLOAD_SYNC,
                        gson.toJson(env)
                    )
                    //
                    val rec = gson.fromJson(
                        response,
                        BigFileRec::class.java
                    )

                    if (checkValidation(rec.validation)) {
                        val bigFilePreferenceManager = BigFilePreferenceManager(context, FILE_TYPE_TICKET)
                        val ticketBigFile = bigFilePreferenceManager.getBigFile()
                        val bigFileResult = ticketBigFile.copy(
                            fileType = rec.file.fileType,
                            fileStatus = BigFileStatus.PROCESS.name,
                            fileCode = rec.file.fileCode
                        )
                        bigFilePreferenceManager.saveBigFile(bigFileResult)
                        return DataOutput.Success(bigFileResult)
                    } else {
                        return formatErrorMsg("Erro! Validation: ${rec.validation}")
                    }

            } else {
                val resultBigFile = updateBigFileStatus(FILE_TYPE_TICKET, BigFileStatus.DONE)
                return DataOutput.Success(resultBigFile)
            }
        } catch (exception: Exception) {
            ToolBox_Inf.registerException(javaClass.name, exception)
            return formatErrorMsg("falha: ${exception.message}")
        }
    }

    private fun getTicketCacheForBigFile(context: Context): MutableList<T_TK_Ticket_Download_PK_Env> {
        return tkTicketCachedao.getSyncBigFile(ToolBox_Con.getPreference_Site_Code(context).toString())
    }

    private fun debugWithNotification(msg: String) {
//        ToolBox.registerException("BIG_FILE", Exception(msg))
    }

}