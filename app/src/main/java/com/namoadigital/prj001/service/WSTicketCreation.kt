package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException

class WSTicketCreation:
    IntentService("WS_Workgroup_Member_List")
{
    //
    private val ticketDao by lazy{
        TK_TicketDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        )
    }
    //
    private val serialDao by lazy{
        MD_Product_SerialDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        )
    }
    //
    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            val bundle = intent?.extras ?: Bundle()
            val customer_code = bundle.getLong(WS_BUNDLE_CUSTOMER_CODE,-1)
            val type_code = bundle.getInt(WS_BUNDLE_TYPE_CODE,-1)
            val site_code = bundle.getInt(WS_BUNDLE_SITE_CODE,-1)
            val operation_code = bundle.getLong(WS_BUNDLE_OPERATION_CODE,-1)
            val product_code = bundle.getLong(WS_BUNDLE_PRODUCT_CODE,-1)
            val serial_code = bundle.getLong(WS_BUNDLE_SERIAL_CODE,-1)
            val comments = bundle.getString(WS_BUNDLE_COMMENTS,"")
            //
            processTicketCreation(customer_code,
                type_code,
                site_code,
                operation_code,
                product_code,
                serial_code,
                comments)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            //
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            ToolBox.sendBCStatus(
                applicationContext,
                ConstantBase.PD_TYPE_ERROR_1,
                sb.toString(),
                "",
                "0"
            )
        } finally {
            WBR_Workgroup_Member_List.completeWakefulIntent(intent)
        }
    }

    @Throws(IOException::class)
    private fun processTicketCreation(
        customerCode: Long,
        typeCode: Int,
        siteCode: Int,
        operationCode: Long,
        productCode: Long,
        serialCode: Long,
        comments: String
    ) {
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_sending_data_msg"], "", "0")
        //
        val token = ToolBox_Inf.getToken(applicationContext)
        var ticket = ArrayList<TicketCreationParams>()
        ticket.add(
            TicketCreationParams(
                customerCode = customerCode,
                typeCode = typeCode,
                siteCode = siteCode,
                operationCode = operationCode,
                productCode = productCode,
                serialCode = serialCode,
                comments = comments
            )
        )
        //
        val env = TicketCreationEnv(
            app_code = Constant.PRJ001_CODE,
            app_version = Constant.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = Constant.PKG_APP_TYPE_DEFAULT,
            token = token,
            ticket = ticket
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_TICKET_CREATION,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_receiving_data_msg"], "", "0")
        /**
         * BARRIONUEVO 2022-08-23
         * Ao retornar do servidor com sucesso setar a preferencia de fluxo offline para false.
         */
        ToolBox_Con.setBooleanPreference(
            applicationContext,
            ConstantBaseApp.PREFERENCE_SERIAL_OFFLINE_FLOW,
            false
        )
        //
        val rec = gson.fromJson(resultado, T_TK_Ticket_Download_Rec::class.java)
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
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_processing_data"], "", "0")
        //
        processTicketCreationReturn(rec.ticket)
    }

    @Throws(IOException::class)
    private fun processTicketCreationReturn(ticketList: java.util.ArrayList<TK_Ticket>?) {
        if (ticketList != null) {
            val hmAux = HMAux()
            val tickets: MutableList<TK_Ticket> = java.util.ArrayList()
            //
            if (ticketList.size == 0) {
                ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAuxTrans["msg_no_data_returned"], hmAux, "", "0")
            } else {
                //
                for (tkTicket in ticketList) {
                    tkTicket.setPK()
                    tickets.add(tkTicket)
                    if (ticketList.size == 1) {
                        hmAux[TK_TicketDao.TICKET_PREFIX] = tkTicket.ticket_prefix.toString()
                        hmAux[TK_TicketDao.TICKET_CODE] = tkTicket.ticket_code.toString()
                    }
                    if (!tkTicket.getSerial().isEmpty()) {
                        for (serial in tkTicket.getSerial()) {
                            serial.log_date = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
                            serialDao.addUpdateTmp(serial)
                            if (!serial.structure.isEmpty()) {
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
                //
                var daoObjReturn = DaoObjReturn()
                //
                if (tickets != null && !tickets.isEmpty()) {
                    daoObjReturn = ticketDao.addUpdate(tickets, false)
                }
                //
                if (!daoObjReturn.hasError()) {
                    ToolBox_Inf.startPdfPhotoDownloadWorkers(applicationContext)
                    //
                    ToolBox.sendBCStatus(applicationContext, "CLOSE_ACT", hmAuxTrans["generic_process_finalized_msg"], hmAux, "", "0")
                } else {
                    ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAuxTrans["msg_error_on_insert_ticket"], HMAux(), "", "0")
                }

            }
        } else {
            ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAuxTrans["msg_no_data_returned"], HMAux(), "", "0")
        }
    }

    private fun loadTranslation() : HMAux {
        val translist = listOf<String>(
            "generic_sending_data_msg",
            "generic_receiving_data_msg",
            "generic_processing_data",
            "generic_process_finalized_msg",
            "msg_no_data_returned"
        )
        //
        val mResourceCode = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModuleCode,
            mResourceName
        )
        //
        return ToolBox_Inf.setLanguage(
            applicationContext,
            mModuleCode,
            mResourceCode,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            translist
        )
    }

    companion object{
        const val WS_BUNDLE_CUSTOMER_CODE = "CUSTOMER_CODE"
        const val WS_BUNDLE_TYPE_CODE = "TYPE_CODE"
        const val WS_BUNDLE_SITE_CODE = "SITE_CODE"
        const val WS_BUNDLE_OPERATION_CODE = "OPERATION_CODE"
        const val WS_BUNDLE_PRODUCT_CODE = "PRODUCT_CODE"
        const val WS_BUNDLE_SERIAL_CODE = "SERIAL_CODE"
        const val WS_BUNDLE_COMMENTS = "COMMENTS"
    }
}