package com.namoadigital.prj001.service.next_os

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.model.SM_SO
import com.namoadigital.prj001.model.next_os.SetReservedUserEnv
import com.namoadigital.prj001.model.next_os.SetReservedUserRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.ui.act047.model.ReservedUser
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WSSoSetReservedUser : BaseWsIntentService("WSSoSetReservedUserReceiver",BaseWsIntentService.IntentServiceMode.DOWNLOAD_DATA()){

    override fun onHandleIntent(intent: Intent?) {
        try {
            val extras = intent ?.extras
            val request = Gson().fromJson(
                extras?.getString(SetReservedUserEnv::class.java.name),
                SetReservedUserEnv:: class.java
            )
            process(request)
        } catch (e:Exception){
            wsExceptionTreatment(e)
        } finally{
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun process(request:SetReservedUserEnv) {
        val gson = GsonBuilder().serializeNulls().create()

        val env = request.apply {
            session_app = getUserSessionAPP()
        }


        val result = ToolBox_Con.connWebService(
            Constant.SET_LIST_USER,
            gson.toJson(env)
        )

        sendBCStatus(
            WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                message = genericServiceTranslate["generic_processing_data"],
                required = "0"
            )
        )

        val gsonParse = GsonBuilder().serializeNulls().create()
        val rec = gsonParse.fromJson(result, SetReservedUserRec:: class.java)


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
        //
        val reservedUser: ReservedUser = parseReservedUser(rec.so_return[0])
        //
        sendBCStatus(WsTypeStatus.CLOSE_ACT(gson.toJson(reservedUser)))
    }

    private fun parseReservedUser(soReturn: SetReservedUserRec.SoReturn): ReservedUser {
        return ReservedUser(
            soReturn.reserved_date,
            soReturn.reserved_user,
            soReturn.reserved_user_nick,
            soReturn.reserved_user_name,
            soReturn.ret_status?:"",
            soReturn.ret_msg?:"",
            soReturn.so_scn
        )
    }
}