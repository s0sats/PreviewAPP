package com.namoadigital.prj001.service.ticket

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.ticket.TkSerialTmpSetRequest
import com.namoadigital.prj001.model.ticket.TkSerialTmpSetResponse
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WsTicketSerialTmpSet : BaseWsIntentService("WsSearchSerialList", IntentServiceMode.UPLOAD_DATA()) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(TkSerialTmpSetRequest.WS_BUNDLE_KEY),
                TkSerialTmpSetRequest::class.java
            )
            process(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun process(request: TkSerialTmpSetRequest) {
        val dao = TK_TicketDao(applicationContext)
        val manager = TokenManager<TkSerialTmpSetRequest>(this)
        val token = manager.getToken(request)

        val model = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }

        connectWS<ApiResponse<TkSerialTmpSetResponse>>(
            url = Constant.WS_SERIAL_TMP_SET,
            model = model
        ) {
            it.watchStatus(
                success = { _ ->
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    try {
                        sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                    }catch (e: Exception){
                        sendBCStatus(WsTypeStatus.ERROR(e.message))
                    }
                },
                failed = { throwable ->
                    sendBCStatus(WsTypeStatus.ERROR(throwable.message))
                }
            )
        }
    }
}
