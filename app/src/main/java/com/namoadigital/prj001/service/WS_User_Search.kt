package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ToolBox.sendBCStatus
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.TUser_Search_Env
import com.namoadigital.prj001.model.TUser_Search_Rec
import com.namoadigital.prj001.receiver.WBR_User_Search
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

class WS_User_Search : IntentService("WS_User_Search") {

    private val hmAux_Trans by lazy {

        val transList: MutableList<String> = ArrayList()
        transList.add("msg_no_data_returned")
        transList.add("generic_sending_data_msg")
        transList.add("generic_receiving_data_msg")
        transList.add("generic_processing_data")
        transList.add("generic_process_finalized_msg")

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

    private val mModule_Code: String = Constant.APP_MODULE
    private var mResource_Code = "0"
    private val mResource_Name = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        val bundle = intent!!.extras
        //
        try {
            val profile_check = bundle!!.getString(Constant.WS_PROFILE_CHECK_FIELD, "1")
            val name = bundle.getString(Constant.WS_USER_NAME_FIELD, "")
            val email = bundle.getString(Constant.WS_USER_EMAIL_FIELD, "")
            val user_code = bundle.getString(Constant.WS_USER_CODE_FIELD, "")
            val erp_code = bundle.getString(Constant.WS_ERP_CODE_FIELD, "")
            process_Search(profile_check, name, email, user_code, erp_code)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            ToolBox_Inf.sendBCStatus(applicationContext, "CUSTOM_ERROR", sb.toString(), "", "0")
        } finally {
            WBR_User_Search.completeWakefulIntent(intent)
        }
    }

    private fun process_Search(
        profileCheck: String,
        name: String,
        email: String,
        userCode: String,
        erpCode: String
    ) {
        //
        val env = TUser_Search_Env(
            profileCheck,
            userCode,
            email,
            name,
            erpCode,
            Constant.PRJ001_CODE,
            Constant.PRJ001_VERSION,
            Constant.PKG_APP_TYPE_DEFAULT,
            ToolBox_Con.getPreference_Session_App(applicationContext)
        )
        //
        ToolBox_Inf.sendBCStatus(
            applicationContext, "STATUS",
            hmAux_Trans["generic_receiving_data_msg"], "", "0"
        )
        //
        val resultado: String = ToolBox_Con.connWebService(
            Constant.WS_USER_LIST_SEARCH,
            gson.toJson(env)
        )
        //
        val rec: TUser_Search_Rec = gson.fromJson(
            resultado,
            TUser_Search_Rec::class.java
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
        //
        sendBCStatus(applicationContext, "STATUS", hmAux_Trans["generic_processing_data"], "", "0")
        //
        processReturn(rec)
    }
    //
    private fun processReturn(rec: TUser_Search_Rec?) {
        rec?.let {
            it.record?.let {
                if(it.size >0) {
                    ToolBox_Inf.sendBCStatus(
                        applicationContext,
                        "CLOSE_ACT",
                        hmAux_Trans["generic_process_finalized_msg"],
                        gson.toJson(rec),
                        "0"
                    )
                }else{
                    handleError()
                }
            } ?: handleError()
        } ?: handleError()
    }
    //
    private fun handleError() {
        ToolBox_Inf.sendBCStatus(
            applicationContext, "ERROR_1",
            hmAux_Trans["msg_no_data_returned"], "", "0"
        )
    }
}