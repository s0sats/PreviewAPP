package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.EV_UserDao
import com.namoadigital.prj001.model.T_Workgroup_Member_List_Env
import com.namoadigital.prj001.model.T_Workgroup_Member_List_Rec
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import java.io.IOException

class WS_Workgroup_Member_List :
    IntentService("WS_Workgroup_Member_List")
{
    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    private val hmAuxTrans:HMAux by lazy {
        loadTranslation()
    }
    
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            val bundle = intent?.extras ?: Bundle()
            val userCode = bundle.getInt(EV_UserDao.USER_CODE,-1)
            //
            processWorkgroupMemberList(userCode)
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
    private fun processWorkgroupMemberList(userCode: Int) {
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_sending_data_msg"], "", "0")
        //
        val env = T_Workgroup_Member_List_Env(
            app_code = Constant.PRJ001_CODE,
            app_version = Constant.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = Constant.PKG_APP_TYPE_DEFAULT,
            userCode = userCode
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_WORKGROUP_MEMBER_LIST,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_receiving_data_msg"], "", "0")
        //
        val rec = gson.fromJson(resultado,T_Workgroup_Member_List_Rec::class.java)
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
        processWorkgroupMemberListReturn(rec)
    }

    @Throws(IOException::class)
    private fun processWorkgroupMemberListReturn(rec: T_Workgroup_Member_List_Rec?) {
        if(rec != null){
            if(rec.data.isNullOrEmpty()){
                ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAuxTrans["msg_no_data_returned"], HMAux(), "", "0")
            }else{
                createWorkgroupListJsonFile(ConstantBaseApp.MD_WORKGROUP_MEMBER_LIST_JSON_FILE, gson.toJson(rec.data))
                //
                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    hmAuxTrans["generic_process_finalized_msg"],
                    HMAux(),
                    ConstantBaseApp.MD_WORKGROUP_MEMBER_LIST_JSON_FILE,
                    "0"
                )
            }
        }else{
            ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAuxTrans["msg_no_data_returned"], HMAux(), "", "0")
        }
    }

    @Throws(IOException::class)
    private fun createWorkgroupListJsonFile(fileName: String, workGroupList: String): File {
        val json_file = File(ConstantBaseApp.TICKET_JSON_PATH, fileName)
        ToolBox_Inf.writeIn(workGroupList, json_file)
        return json_file
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

}