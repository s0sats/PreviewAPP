package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiPageRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse.ApiCollection
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.AvailableUsersRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WsGetUsersAvailable : BaseWsIntentService("WsGetUsersAvailable", IntentServiceMode.DOWNLOAD_DATA()) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            process()
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun process() {
        val gson = GsonBuilder().serializeNulls().create()

        val env = ApiPageRequest<Any>().apply {
            session_app = getUserSessionAPP()
        }


        connectWS<ApiResponse<ApiCollection<AvailableUsersRec>>>(
            url = Constant.WS_TRIP_AVAILABLE_USERS,
            model = env
        ) {
            it.watchStatus(
                success = { response ->
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    val list = gson.toJson(response.data?.list)
                    sendBCStatus(WsTypeStatus.CLOSE_ACT(list))
                },
            )
        }
    }

}