package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.getToken
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.GetTripFullEnv
import com.namoadigital.prj001.receiver.trip.WBR_CreateTrip
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WsGetTripFull : BaseWsIntentService("WsGetTripFull", IntentServiceMode.DOWNLOAD_DATA()) {

    override fun onHandleIntent(intent: Intent?) {

        try {
            //
            processCreateTrip()
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            WBR_CreateTrip.completeWakeFulService(intent)
        }
    }
    @Throws
    private fun processCreateTrip() {
        val dao = FSTripDao(this)
        val trip = dao.getTrip()

        val modelEnv = ApiRequest(
            token = getToken(),
            parameters = GetTripFullEnv(
                trip!!.tripPrefix,
                trip!!.tripCode
            )
        ).apply {
            session_app = getUserSessionAPP()
        }
        //
        connectWS<ApiResponse<FSTrip>>(
            url = Constant.WS_TRIP,
            model = modelEnv
        ) {
            it.watchStatus(
                success = { response ->
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    response.data?.let { trip ->
                        dao.syncTripFull(
                            trip
                        )
                        //
                        ToolBox.sendBCStatus(
                            applicationContext,
                            "CLOSE_ACT",
                            genericServiceTranslate["generic_process_finalized_msg"],
                            HMAux(),
                            "",
                            "0"
                        )
                        //
                    } ?: run {
                        if (response.status?.code == 406) {
                            ToolBox.sendBCStatus(
                                this,
                                "CUSTOM_ERROR",
                                response.status?.message,
                                "",
                                ""
                            )
                        }
                    }
                },
            )
        }
    }
}
