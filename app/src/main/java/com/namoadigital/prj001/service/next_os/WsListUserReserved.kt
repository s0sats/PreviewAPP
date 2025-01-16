package com.namoadigital.prj001.service.next_os

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.model.next_os.ListReservedUserEnv
import com.namoadigital.prj001.model.next_os.ListReservedUserRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WsListUserReserved : BaseWsIntentService("WsListUserReserved", IntentServiceMode.DOWNLOAD_DATA()) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(ListReservedUserEnv::class.java.name),
                ListReservedUserEnv::class.java
            )
            process(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun process(request: ListReservedUserEnv) {
        val gson = GsonBuilder().serializeNulls().create()

        val env = request.apply {
            session_app = getUserSessionAPP()
        }


        val result = ToolBox_Con.connWebService(
            Constant.RESERVED_LIST_USER,
            gson.toJson(env)
        )

        sendBCStatus(
            WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                message = genericServiceTranslate["generic_processing_data"],
                required = "0"
            )
        )

        val gsonParse = GsonBuilder().serializeNulls().create()
        val rec = gsonParse.fromJson(result, ListReservedUserRec::class.java)


        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                1,
                1
            ) || !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg
            )
        ) {
            return
        }


        sendBCStatus(
            WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                message = genericServiceTranslate["generic_processing_data"],
                required = "0"
            )
        )
        val list = gson.toJson(rec.result?.users)
        sendBCStatus(WsTypeStatus.CLOSE_ACT(list))
    }

}