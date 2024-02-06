package com.namoadigital.prj001.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.CheckType
import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase.Companion.SiteInventoryUseCaseFactory
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException

class WsScheduleNotExecuted :
    BaseWsIntentService("WS_Schedule_Not_Executed", IntentServiceMode.UPLOAD_DATA()) {

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
            val schedule_prefix = bundle.getInt(WS_BUNDLE_SCHEDULE_PREFIX, -1)
            val schedule_code = bundle.getInt(WS_BUNDLE_SCHEDULE_CODE, -1)
            val schedule_exec = bundle.getInt(WS_BUNDLE_SCHEDULE_EXEC, -1)
            val comments = bundle.getString(WS_BUNDLE_COMMENTS, "")
            val justify_group_code = bundle.getInt(WS_BUNDLE_JUSTIFY_GROUP_CODE, -1)
            val justify_item_code = bundle.getInt(WS_BUNDLE_JUSTIFY_ITEM_CODE, -1)
            val reschedule_date = bundle.getString(WS_BUNDLE_RESCHEDULE_DATE, "")

            //
            processScheduleNotExecuted(
                schedule_prefix,
                schedule_code,
                schedule_exec,
                comments,
                justify_group_code,
                justify_item_code,
                reschedule_date,
            )
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
    private fun processScheduleNotExecuted(
        schedule_prefix: Int,
        schedule_code: Int,
        schedule_exec: Int,
        comments: String,
        justify_group_code: Int,
        justify_item_code: Int,
        reschedule_date: String
    ) {
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_sending_data_msg"],
            "",
            "0"
        )
        //
        val token = ToolBox_Inf.getToken(applicationContext)
        //
        val env = ScheduleNotExecutedEnv(
            app_code = ConstantBaseApp.PRJ001_CODE,
            app_version = ConstantBaseApp.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = ConstantBaseApp.PKG_APP_TYPE_DEFAULT,
            token = token,
            customer_code = ToolBox_Con.getPreference_Customer_Code(applicationContext),
            schedule_prefix = schedule_prefix,
            schedule_code = schedule_code,
            schedule_exec = schedule_exec,
            comments = comments,
            justify_group_code = justify_group_code,
            justify_item_code = justify_item_code,
            reschedule_date = reschedule_date,
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_SCHEDULE_NOT_EXECUTED,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_receiving_data_msg"],
            "",
            "0"
        )
        //
        val rec = gson.fromJson(resultado, ScheduleNotExecutedRec::class.java)
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
        updateSerialSiteInventoryPrefs()
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_processing_data"],
            "",
            "0"
        )
        //
        processScheduleNotExecutedReturn(
            schedule_prefix,
            schedule_code,
            schedule_exec,
            rec.error_msg
        )
    }

    @Throws(IOException::class)
    private fun processScheduleNotExecutedReturn(
        schedule_prefix: Int,
        schedule_code: Int,
        schedule_exec: Int,
        errorMsg: String?
    ) {
        updateScheduleExec(
            schedule_prefix = schedule_prefix,
            schedule_code = schedule_code,
            schedule_exec = schedule_exec,
        )
        ToolBox.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            hmAuxTrans["generic_process_finalized_msg"],
            HMAux(),
            errorMsg,
            "0"
        )
    }

    private fun updateSerialSiteInventoryPrefs() {
        val useCase = SiteInventoryUseCaseFactory(applicationContext).editPrefrenceFileUseCase()
        if (useCase.check?.invoke(CheckType.FILE_EXIST) == true) {
            val editPref = HashMap<String, Any>()
            editPref["refresh"] = true
            useCase.editPreference!!(editPref)
        }
    }

    private fun loadTranslation(): HMAux {
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


    private fun updateScheduleExec(
        schedule_prefix: Int,
        schedule_code: Int,
        schedule_exec: Int,
    ) {

        //
        val dao = MD_Schedule_ExecDao(
            applicationContext,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(applicationContext)),
            Constant.DB_VERSION_CUSTOM
        )

        dao.getByString(
            MD_Schedule_Exec_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(applicationContext),
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery()
        )?.let {
            it.status = ConstantBaseApp.SYS_STATUS_NOT_EXECUTED
            dao.addUpdate(it)
        }


    }

    companion object {
        const val WS_BUNDLE_CUSTOMER_CODE = "CUSTOMER_CODE"
        const val WS_BUNDLE_SCHEDULE_PREFIX = "SCHEDULE_PREFIX"
        const val WS_BUNDLE_SCHEDULE_CODE = "SCHEDULE_CODE"
        const val WS_BUNDLE_SCHEDULE_EXEC = "SCHEDULE_EXEC"
        const val WS_BUNDLE_COMMENTS = "COMMENTS"
        const val WS_BUNDLE_JUSTIFY_GROUP_CODE = "JUSTIFY_GROUP_CODE"
        const val WS_BUNDLE_JUSTIFY_ITEM_CODE = "JUSTIFY_ITEM_CODE"
        const val WS_BUNDLE_RESCHEDULE_DATE = "RESCHEDULE_DATE"
    }
}