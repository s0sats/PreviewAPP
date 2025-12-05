package com.namoadigital.prj001.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.CheckType
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase.Companion.SiteInventoryUseCaseFactory
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.extensions.listToHashMap
import com.namoadigital.prj001.extensions.sendFCMStatus
import com.namoadigital.prj001.extensions.ticket.processTicketAndSerialConciliation
import com.namoadigital.prj001.extensions.updateSerialSiteInventoryRefresh
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Env
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec
import com.namoadigital.prj001.model.ticket.TkTicketToSync
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.singleton.ticket.TicketDownloadRestriction


class WorkDownloadTicket(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val ticketDao = TK_TicketDao(context)
    private val tkTicketCachedao = TkTicketCacheDao(context)

    override fun doWork(): Result {
        //
//        Log.d("SYNC_STRUCTURE", "isStopped: $isStopped")
        if (ToolBox_Con.getPreference_Customer_Code(context) == -1L
            || ToolBox_Con.getPreference_User_Code(context).isBlank()
            || isStopped
        ) {
            return Result.success()
        }
        //
        try {
            var result = true
            //
            val syncList = ticketDao.syncList
            syncList.addAll(getSyncList())
            //
            val tkTicketToSyncs = if(TicketDownloadRestriction.isTicketDownloadRestrictionInitialized()) {
                syncList.filter {
                    !(it.ticketPrefix == TicketDownloadRestriction.ticketPrefixRestriction
                            && it.ticketCode == TicketDownloadRestriction.ticketCodeRestriction)
                }
            }else{
                syncList
            }
            //
            if (tkTicketToSyncs.isNotEmpty()){
                val tickets: HashMap<Int, List<TkTicketToSync>> = listToHashMap(tkTicketToSyncs, 10)
                //
                for ((page, list) in tickets) {
//                    Log.d("SYNC_STRUCTURE", "-------Loop--------------------")
//                    Log.d("SYNC_STRUCTURE", "progress: ${page}/${tickets.size}")
//                    Log.d("SYNC_STRUCTURE", "isStopped: $isStopped")
                    //
                    if(!isStopped) {
                        result = syncTickets(list)
                        if (!result) {
                            break
                        }
                    }else{
//                        Log.d("SYNC_STRUCTURE", "Result.success by isStopped: $isStopped")
                        return Result.success()
                    }
                }
                //
            }
            //

            return if (result){
                context.sendFCMStatus(ConstantBaseApp.FCM_MODULE_TICKET)
                val hasTicketAfterDownload = getSyncList().isNotEmpty() || ticketDao.syncList.isNotEmpty()
                if(hasTicketAfterDownload){
                    Result.retry()
                }else {
                    Result.success()
                }
            }else{
                Result.failure()
            }
            //
        } catch (httpException: NetworkConnectionException){
            ToolBox_Inf.registerException(javaClass.name, httpException)
//            Log.d("SYNC_STRUCTURE", "-------NetworkConnectionException---------")
            return Result.retry()
        } catch (e: Exception){
            ToolBox_Inf.registerException(javaClass.name, e)
//            Log.d("SYNC_STRUCTURE", "-------Exception---------")
//            Log.d("SYNC_STRUCTURE", "Exception: ${e.message}")
            return Result.failure()
        }
    }

    private fun getSyncList() =
        tkTicketCachedao.getSyncTicket(ToolBox_Con.getPreference_User_Code(context))


    private fun checkValidation(validation: String?): Boolean {

        when (validation) {
            "VERSION_ERRO",
            "VERSION_INVALID",
            "EXPIRED",
            "LOGIN_ERRO",
            "USER_INVALID",
            "USER_CANCELLED",
            "SESSION_NOT_FOUND",
            "USER_BLOCKED",
            "CREATE_SESSION_ABORT",
            "LICENSE_QTY_INVALID",
            "PARAMETERS_ERROR",
            "CUSTOMER_IP_REQUIRED",
            "CUSTOMER_IP_RESTRICTION",
            null,
            -> {
                ToolBox_Con.setPreference_Status_Login(context,Constant.LOGIN_STATUS_SESSION_NOT_FOUND);
                return false
            }
        }

        return true
    }
    @Throws(Exception::class)
    private fun syncTickets(tkTicketToSyncs: List<TkTicketToSync>):Boolean {
        val env = T_TK_Ticket_Download_Env()
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT
        env.ticket.addAll(getTicketToEnv(tkTicketToSyncs))
        env.login = 0
        //
        val gson = GsonBuilder().serializeNulls().create()
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_DOWNLOAD,
            gson.toJson(env),
            ConstantBaseApp.TIMEOUT_FOR_SYNC_FULL
        )
        //
        val rec = gson.fromJson(
            resultado,
            T_TK_Ticket_Download_Rec::class.java
        )

        if(!checkValidation(rec.validation)
            || !rec.error_msg.isNullOrBlank()){
            return false
        }
        //
        val useCase = SiteInventoryUseCaseFactory(applicationContext).editPrefrenceFileUseCase()
        if (useCase.check!!.invoke(CheckType.FILE_EXIST)) {
            useCase.updateSerialSiteInventoryRefresh(true)
        }
        //
        val ticketList = rec.ticket
        val ticketDao = TK_TicketDao(applicationContext)
        //
        return ticketDao.processTicketAndSerialConciliation(
            applicationContext,
            ticketList,
            tkTicketCachedao,
            MD_Product_SerialDao(applicationContext)
        )
    }

    private fun getTicketToEnv(tkTicketToSyncs: List<TkTicketToSync>): List<T_TK_Ticket_Download_PK_Env> {
         return tkTicketToSyncs.map {
            val tTkTicketDownloadPkEnv = T_TK_Ticket_Download_PK_Env()
            tTkTicketDownloadPkEnv.customer_code = it.customerCode
            tTkTicketDownloadPkEnv.ticket_prefix = it.ticketPrefix
            tTkTicketDownloadPkEnv.ticket_code = it.ticketCode
            tTkTicketDownloadPkEnv.scn = it.scn
            tTkTicketDownloadPkEnv
        }
    }

    companion object{
        val WORKER_TAG = "WorkDownloadTicket"
    }

}
