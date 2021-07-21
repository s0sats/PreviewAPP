package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.T_Workgroup_Member_Edit_Env
import com.namoadigital.prj001.model.T_Workgroup_Member_Edit_Rec
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_Edit
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException
import java.util.*

class WS_Workgroup_Member_Edit :
    IntentService("WS_Workgroup_Member_Edit")
{
    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_workgroup_member_edit"
    private val gson = GsonBuilder().serializeNulls().create()
    private val hmAuxTrans:HMAux by lazy {
        loadTranslation()
    }
    
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            val bundle = intent?.extras ?: Bundle()
            val userCode = bundle.getInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.USER_CODE,-1)
            val action = bundle.getInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.ACTIVE,0)
            val limit = bundle.getInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.LIMIT)
            val dateExpire = bundle.getString(T_Workgroup_Member_Edit_Env.WorkgroupSetData.DATE_EXPIRE)
            val expireReturn = bundle.getInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.EXPIRE_RETURN)
            val groupCodeList = bundle.getIntegerArrayList(T_Workgroup_Member_Edit_Env.WorkgroupSetData.GROUP_CODE)?: arrayListOf()
            //
            processWorkgroupMemberEdit(userCode,action,limit,dateExpire,expireReturn,groupCodeList)

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
            WBR_Workgroup_Member_Edit.completeWakefulIntent(intent)
        }
    }

    @Throws(IOException::class)
    private fun processWorkgroupMemberEdit(
        userCode: Int,
        action: Int,
        limit: Int,
        dateExpire: String?,
        expireReturn: Int,
        groupCodeList: ArrayList<Int>
    ) {
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_sending_data_msg"], "", "0")
        //
        val env = T_Workgroup_Member_Edit_Env(
            app_code = Constant.PRJ001_CODE,
            app_version = Constant.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = Constant.PKG_APP_TYPE_DEFAULT,
            data = T_Workgroup_Member_Edit_Env.WorkgroupSetData(
                userCode = userCode,
                active = action,
                limit = limit,
                dateExpire = dateExpire,
                expireReturn = expireReturn,
                groupCodeList = groupCodeList
            )
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_WORKGROUP_MEMBER_EDIT,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_receiving_data_msg"], "", "0")
        //
        val rec = gson.fromJson(resultado, T_Workgroup_Member_Edit_Rec::class.java)
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
        processWorkgroupMemberEditReturn(rec)
    }

    @Throws(IOException::class)
    private fun processWorkgroupMemberEditReturn(rec: T_Workgroup_Member_Edit_Rec?) {
        if(rec != null){
            if(rec.status == null || ConstantBaseApp.MAIN_RESULT_OK != rec.status){
                ToolBox.sendBCStatus(
                    applicationContext,
                    "ERROR_1",
                    rec.error_msg ?: hmAuxTrans["msg_no_data_returned"],
                    HMAux(),
                    "",
                    "0"
                )
            }else{
                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    hmAuxTrans["generic_process_finalized_msg"],
                    HMAux(),
                    ConstantBaseApp.MAIN_RESULT_OK,
                    "0"
                )
            }
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
            "msg_error_on_insert_ticket",
            "msg_no_data_returned",
            "msg_error_on_update_ticket_schedule_infos"
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