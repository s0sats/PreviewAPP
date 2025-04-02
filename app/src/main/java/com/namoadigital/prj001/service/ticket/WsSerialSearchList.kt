package com.namoadigital.prj001.service.ticket

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse.ApiCollection
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.ticket.TkSerialSearchListResponse
import com.namoadigital.prj001.model.ticket.TkSerialSearchRequest
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WsSerialSearchList : BaseWsIntentService("WsSerialSearchList", IntentServiceMode.DOWNLOAD_DATA()) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(TkSerialSearchRequest.WS_BUNDLE_KEY),
                TkSerialSearchRequest::class.java
            )
            process(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun process(request: TkSerialSearchRequest) {
        val manager = TokenManager<TkSerialSearchRequest>(this)
        val token = manager.getToken(request)
        val gson = GsonBuilder().serializeNulls().create()

        val model = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }

        connectWS<ApiResponse<ApiCollection<TkSerialSearchListResponse>>>(
            url = Constant.WS_SEARCH_SERIAL_LIST,
            model = model
        ) {
            it.watchStatus(
                success = { response ->
                    manager.deleteToken()
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )



                    val result = if(response.data == null){
                        "[]"
                    }else{
                        gson.toJson(response.data)
                    }
                    sendBCStatus(WsTypeStatus.CLOSE_ACT(result))
                },
                failed = { throwable ->
                    manager.deleteToken()
                    sendBCStatus(WsTypeStatus.ERROR(throwable.message))
                }
            )
        }
    }

}