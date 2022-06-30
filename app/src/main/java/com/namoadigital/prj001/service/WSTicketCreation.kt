package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.TicketCreationEnv
import com.namoadigital.prj001.model.TicketCreationParams
import com.namoadigital.prj001.model.TicketCreationRec
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException

class WSTicketCreation:
    IntentService("WS_Workgroup_Member_List")
{
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
            val serial_code = bundle.getInt(WS_BUNDLE_SERIAL_CODE,-1)
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
        serialCode: Int,
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
        //
        val rec = gson.fromJson(resultado, TicketCreationRec::class.java)
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
        processTicketCreationReturn(rec)
    }




    @Throws(IOException::class)
    private fun processTicketCreationReturn(rec: TicketCreationRec?) {
        if(rec != null
            && rec.ticketPrefix != null
            && rec.ticketCode != null
        ){
            //
            ToolBox.sendBCStatus(
                applicationContext,
                "CLOSE_ACT",
                hmAuxTrans["generic_process_finalized_msg"],
                HMAux(),
                gson.toJson(rec),
                "0"
            )
            //
        }else{
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