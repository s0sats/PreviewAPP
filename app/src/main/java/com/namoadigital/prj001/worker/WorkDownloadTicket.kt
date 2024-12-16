package com.namoadigital.prj001.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.CheckType
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase.Companion.SiteInventoryUseCaseFactory
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.MD_SiteDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_ActionDao
import com.namoadigital.prj001.dao.TK_Ticket_ApprovalDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.dao.TK_Ticket_StepDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.extensions.sendFCMStatus
import com.namoadigital.prj001.extensions.updateSerialSiteInventoryRefresh
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Env
import com.namoadigital.prj001.model.T_TK_Ticket_Download_PK_Env
import com.namoadigital.prj001.model.T_TK_Ticket_Download_Rec
import com.namoadigital.prj001.model.ticket.TkTicketToSync
import com.namoadigital.prj001.sql.TK_Ticket_Sql_004
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.singleton.ticket.TicketDownloadRestriction


class WorkDownloadTicket(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private lateinit var fileToDel: String
    private val ticketDao = TK_TicketDao(context)
    private val tkTicketCachedao = TkTicketCacheDao(context)
    private val ticketCtrlDao = TK_Ticket_CtrlDao(context)
    private val ticketStepDao = TK_Ticket_StepDao(context)
    private val ticketActionDao = TK_Ticket_ActionDao(context)
    private val ticketApprovalDao = TK_Ticket_ApprovalDao(context)
    private val scheduleExecDao = MD_Schedule_ExecDao(context)
    private val geFileDao = GE_FileDao(context)
    private val siteDao = MD_SiteDao(context)
    private val serialDao = MD_Product_SerialDao(context)
    private val gsonRec = GsonBuilder().serializeNulls().create()
    private val gsonEnv = GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create()
    private var file_to_del = ""
    private var reSend: Boolean = true
    private val ticketToSend = java.util.ArrayList<TK_Ticket>()
    override fun doWork(): Result {
        Log.d("WorkDownloadTicket", "doWork")
        //
        if(ToolBox_Con.getPreference_Customer_Code(context) == -1L
            || ToolBox_Con.getPreference_User_Code(context).isBlank()){
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
            }.take(10)
            //
            if (tkTicketToSyncs.isNotEmpty()){
               result = syncTickets(tkTicketToSyncs)
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
            return Result.retry()
        } catch (e: Exception){
            ToolBox_Inf.registerException(javaClass.name, e)
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
            gson.toJson(env)
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
        if (ticketList != null && ticketList.size > 0) {
            var daoObjReturn = DaoObjReturn()
            //
            val hmAux = HMAux()
            val tickets: MutableList<TK_Ticket> = ArrayList()
            for (tkTicket in ticketList) {
                tkTicket.setPK()
                val dbTicket: TK_Ticket? = ticketDao.getTicket(
                    tkTicket.customer_code,
                    tkTicket.ticket_prefix,
                    tkTicket.ticket_code
                )
                var saveTicket = true
                dbTicket?.let{
                    /*
                        Barrionuevo - 2020-11-13
                        Tratativa para impedir que ticket com form espontaneo em processo seja atualizado pelo server.
                     */
                    if (ToolBox_Inf.checkTicketForServerUpdate(applicationContext, tkTicket, dbTicket)) {
                        //Verifica se precisa resetar alguma foto. Isso deve ser feito se o "file_code" da foto
                        //for alterado, o que significa que mudaram a foto no server...
                        TK_Ticket.checkActionPhotoResetNeeds(
                            dbTicket,
                            tkTicket
                        )
                        //Varre todas as imagens verificando se existe imagem local para cada item que pode ter foto
                        tkTicket.updateLocalImagesPathIfExists()
                        //Busca ctrls tipo form em andamento e que seriam resetados.
                        tkTicket.updateTicketCtrlFormInProcess(applicationContext)
                        //
                        daoObjReturn = ticketDao.removeFullV2(tkTicket)
                        //                        tickets.add(tkTicket);
                        if (daoObjReturn.hasError()) {
                            return false
                        }
                    }else{
                        saveTicket = false
                    }
                }
                //
                val serialDao = MD_Product_SerialDao(applicationContext)
                if (tkTicket.serial.isNotEmpty()) {
                    for (serial in tkTicket.serial) {
                        val dbSerial: MD_Product_Serial? = serialDao.getSerial(
                            serial.customer_code,
                            serial.product_code,
                            Math.toIntExact(serial.serial_code)
                        )
                        //
                        dbSerial?.let{
                            if(dbSerial.update_required == 1){
                                tkTicket.sync_required = 1
                            }else{
                                serial.log_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
                                serialDao.addUpdateTmp(serial)
                                if (serial.structure.isNotEmpty()) {
                                    serialDao.addFullStructure(serial)
                                } else {
                                    if (serial.has_item_check == 0) {
                                        serialDao.removeFullStructure(serial)
                                    } else {

                                    }
                                }
                            }
                        }
                        //
                        if (dbSerial != null
                            && dbSerial.update_required == 1
                        ) {
                            tkTicket.sync_required = 1
                        } else {
                            serial.log_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
                            serialDao.addUpdateTmp(serial)
                            if (serial.structure.isNotEmpty()) {
                                serialDao.addFullStructure(serial)
                            } else {
                                /**
                                 * BARRIONUEVO 22-11-2021
                                 * VErifica o has_item_check para apagar a structure do serial
                                 */
                                if (serial.has_item_check == 0) {
                                    serialDao.removeFullStructure(serial)
                                }
                            }
                        }
                    }
                }
                if(saveTicket) {
                    tickets.add(tkTicket)
                }
                //
            }
            //
            if (!daoObjReturn.hasError()) {
                if (tickets.isNotEmpty()) {
                    daoObjReturn = ticketDao.addUpdate(tickets, false)
                    for (ticket in tickets) {
                        if (ticket.sync_required == 1) {
                            ticketDao.addUpdate(
                                TK_Ticket_Sql_004(
                                    ticket.customer_code,
                                    ticket.ticket_prefix,
                                    ticket.ticket_code,
                                    1
                                ).toSqlQuery()
                            )
                        }else{
                            tkTicketCachedao.removeCache(
                                ticket.customer_code,
                                ticket.ticket_prefix,
                                ticket.ticket_code
                            )
                        }
                    }
                }
                //
                if (!daoObjReturn.hasError()) {
                    ToolBox_Inf.startPdfPhotoDownloadWorkers(applicationContext)
                    //
                    return true
                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return true
        }
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
