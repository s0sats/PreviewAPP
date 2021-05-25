package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Search_Not_Focus
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001
import com.namoadigital.prj001.sql.TK_Ticket_Sql_011
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

public class WS_TK_Ticket_Search_Not_Focus : IntentService("WS_TK_Ticket_Search_Not_Focus") {

    val RETURNED_TICKET_QTY = "RETURNED_TICKET_QTY"

    private val hmAux_Trans by lazy{
        val translist: MutableList<String> = ArrayList()
        //
        //
        translist.add("generic_sending_data_msg")
        translist.add("generic_receiving_data_msg")
        translist.add("generic_processing_data")
        translist.add("generic_process_finalized_msg")
        translist.add("msg_error_on_insert_ticket")
        translist.add("msg_no_data_returned")
        translist.add("msg_error_on_update_ticket_schedule_infos")

        //
        val mResource_Code = ToolBox_Inf.getResourceCode(
                applicationContext,
                mModule_Code,
                mResource_Name
        )

        ToolBox_Inf.setLanguage(
                applicationContext,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(applicationContext),
                translist)
    }
    //
    private val ticketDao by lazy{
        TK_TicketDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        )
    }
    //
    private val scheduleExecDao by lazy{
        MD_Schedule_ExecDao(
                getApplicationContext(),
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
                Constant.DB_VERSION_CUSTOM
        )
    }
    //
    private val mModule_Code = Constant.APP_MODULE
    private val mResource_Name = "ws_tk_ticket_search_not_focus"
    private var gson: Gson = GsonBuilder().serializeNulls().create()


    override fun onHandleIntent(intent: Intent?) {
        val bundle = intent?.getExtras();
        try {
            gson = GsonBuilder().serializeNulls().create()
            val contractId = bundle!!.getString(TK_TicketDao.CONTRACT_ID, "")
            val clientId = bundle.getString(TK_TicketDao.CLIENT_ID, "")
            val ticketId = bundle.getString(TK_TicketDao.TICKET_ID, "")
            val productCode:Int = bundle.getInt(MD_Product_SerialDao.PRODUCT_CODE, -1)
            val serialCode:Int = bundle.getInt(MD_Product_SerialDao.SERIAL_CODE, -1)
            //
            var mProductCode: Int? = null
            if(productCode > 0){
                mProductCode =productCode
            }
            var mSerialCode: Int? = null
            if(serialCode > 0){
                mSerialCode =serialCode
            }
            processTicketNotFocusDownload(contractId, clientId, ticketId, mProductCode, mSerialCode)
        } catch (e: Exception) {
            val sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            ToolBox_Inf.sendBCStatus(applicationContext, "ERROR_1", sb.toString(), "", "0")
        } finally {
            WBR_TK_Ticket_Search_Not_Focus.completeWakefulIntent(intent)
        }
    }

    private fun processTicketNotFocusDownload(contractId: String, clientId: String, ticketId: String, productCode: Int?, serialCode: Int?) {
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAux_Trans["generic_sending_data_msg"], "", "0")
        //
        val env = T_TK_Ticket_Search_Not_Focus_Env()
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT

        env.getSearch().add(
                T_TK_Ticket_Search_Not_Focus_Param(
                        ToolBox_Con.getPreference_Customer_Code(applicationContext),
                        contractId,
                        clientId,
                        ticketId,
                        productCode,
                        serialCode
                )
        )
        //
        val resultado = ToolBox_Con.connWebService(
                Constant.WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS,
                gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAux_Trans["generic_receiving_data_msg"], "", "0")
        //
        //
        val rec = gson.fromJson(
                resultado,
                T_TK_Ticket_Download_Rec::class.java
        )
        //
        //
        if (!ToolBox_Inf.processWSCheckValidation(
                        applicationContext,
                        rec.validation,
                        rec.error_msg,
                        rec.link_url,
                        1,
                        1)
                ||
                !ToolBox_Inf.processoOthersError(
                        applicationContext,
                        resources.getString(R.string.generic_error_lbl),
                        rec.error_msg)) {
            return
        }
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAux_Trans["generic_processing_data"], "", "0")
        //
        //
        processTicketReturn(rec.ticket)
    }

    private fun processTicketReturn(ticketList: ArrayList<TK_Ticket>?) {
        val scheduleExecList = ArrayList<MD_Schedule_Exec>()
        var daoObjReturn = DaoObjReturn()
        val tickets: MutableList<TK_Ticket> = ArrayList()
        if (ticketList != null) {
            val hmAux = HMAux()
            hmAux[WS_TK_Ticket_Client_Contract_Search.RETURNED_TICKET_QTY] = ticketList.size.toString()
            //Se nenhum Ticket retornado, ja envia close act
            if (ticketList.size == 0) {
                ToolBox.sendBCStatus(applicationContext, "CLOSE_ACT", hmAux_Trans["generic_process_finalized_msg"], hmAux, "", "0")
            } else {
                //
                for (tkTicket in ticketList) {
                    tkTicket.setPK()
                    val dbTicket: TK_Ticket? = getDbTicket(tkTicket)
                    if (dbTicket != null) {
                        /*
                        Barrionuevo - 2020-11-13
                        Tratativa para impedir que ticket com form espontaneo em processo seja atualizado pelo server.
                        Barrionuevo - 2021-05-25
                        Verificar se o ticket baixado possui update_required, caso positivo ignora o registro.
                     */
                        val ticketHasUpdateRequired:HMAux  = ticketDao.getByStringHM(
                                TK_Ticket_Sql_011(
                                        dbTicket.customer_code,
                                        dbTicket.ticket_prefix,
                                        dbTicket.ticket_code,
                                ).toSqlQuery()
                        )
                        if (!ToolBox_Inf.hasOffHandFormInProcess(applicationContext, dbTicket.ticket_prefix, dbTicket.ticket_code)
                                && "0".equals(ticketHasUpdateRequired.get(TK_TicketDao.UPDATE_REQUIRED))
                        ) {
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
                            tickets.add(tkTicket)
                            if (daoObjReturn.hasError()) {
                                break
                            }
                        }
                    } else {
                        tickets.add(tkTicket)
                    }
                    if (ticketList.size == 1) {
                        hmAux[TK_TicketDao.TICKET_PREFIX] = tkTicket.ticket_prefix.toString()
                        hmAux[TK_TicketDao.TICKET_CODE] = tkTicket.ticket_code.toString()
                    }
                    //LUCHE - 31/03/2020
                    //Atualiza dados do agendamento
                    if (isScheduledTicket(tkTicket)) {
                        val scheduleExec = getSchedule(
                                tkTicket.schedule_prefix!!,
                                tkTicket.schedule_code!!,
                                tkTicket.schedule_exec!!
                        )
                        //Pode não existir o agendamento, caso o usr não esteja no workgroup
                        //Se status igual, não é necessario mexer.
                        val adjustedStatus = if (tkTicket.ticket_status == ConstantBaseApp.SYS_STATUS_PENDING) ConstantBaseApp.SYS_STATUS_PROCESS else tkTicket.ticket_status
                        //Se existe o agendamento, ele é valido e o status do ticket é diferente do agendamnto
                        //Atualiza status do agendamento e reseta infos de FCM e msg de erro.

                        if (MD_Schedule_Exec.isValidScheduleExec(scheduleExec)
                                && !scheduleExec?.status.equals(adjustedStatus, ignoreCase = true)) {
                            scheduleExec?.status = adjustedStatus
                            scheduleExec?.fcm_new_status = null
                            scheduleExec?.fcm_user_nick = null
                            scheduleExec?.schedule_erro_msg = null
                            scheduleExec?.close_date = null
                            //
                            scheduleExecList.add(scheduleExec!!)
                        }

                    }
                }
                //Se existe agendamento, tenta o insert
                if (scheduleExecList != null && scheduleExecList.size > 0) {
                    daoObjReturn = scheduleExecDao.addUpdate(scheduleExecList, false)
                }
                //Se sucesso, vai para insert do ticket.
                if (!daoObjReturn.hasError()) {
                    //
                    if (tickets != null && !tickets.isEmpty()) {
                        daoObjReturn = ticketDao.addUpdate(tickets, false)
                    }
                    if (!daoObjReturn.hasError()) {
                        ToolBox_Inf.startPdfPhotoDownloadWorkers(applicationContext)
                        //
                        ToolBox.sendBCStatus(applicationContext, "CLOSE_ACT", hmAux_Trans["generic_process_finalized_msg"], hmAux, "", "0")
                    } else {
                        ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAux_Trans["msg_error_on_insert_ticket"], HMAux(), "", "0")
                    }
                } else {
                    ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAux_Trans["msg_error_on_update_ticket_schedule_infos"], HMAux(), "", "0")
                }
            }
        } else {
            ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAux_Trans["msg_no_data_returned"], HMAux(), "", "0")
        }
    }

    private fun getDbTicket(tkTicket: TK_Ticket): TK_Ticket? {
        return ticketDao.getByString(
                TK_Ticket_Sql_001(
                        tkTicket.customer_code,
                        tkTicket.ticket_prefix,
                        tkTicket.ticket_code
                ).toSqlQuery()
        )
    }

    /**
     * LUCHE - 31/03/2020
     *
     *
     * Metodo que verifica se o ticket é um agendado
     * @param tkTicket Ticket
     * @return - True se o ticket for != null e tiver a pk do agendamento.
     */
    private fun isScheduledTicket(tkTicket: TK_Ticket?): Boolean {
        return tkTicket != null && tkTicket.schedule_prefix != null && tkTicket.schedule_code != null && tkTicket.schedule_exec != null
    }

    /**
     * LUCHE - 31/03/2020
     *
     *
     * Metodo que busca o agendamento para o ticket
     * @param schedule_prefix Prefixo do agendamento
     * @param schedule_code Codigo do agendamento
     * @param schedule_exec Exec do agendamento
     * @return Obj agendamento ou null se não encontrar
     */
    private fun getSchedule(schedule_prefix: Int, schedule_code: Int, schedule_exec: Int): MD_Schedule_Exec? {
        return scheduleExecDao.getByString(
                MD_Schedule_Exec_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(applicationContext),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
    }
}