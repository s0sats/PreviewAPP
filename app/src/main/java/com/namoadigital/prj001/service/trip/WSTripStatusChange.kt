package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.Gson
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.extensions.getToken
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeRec
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv
import com.namoadigital.prj001.receiver.trip.WBRTripStatusChange
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.trip.TransactionWsTripDestinationStatusChange
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WSTripStatusChange: BaseWsIntentService("WSTripStatusChange", IntentServiceMode.UPLOAD_DATA()) {

    lateinit var dao: FSTripDao

    override fun onHandleIntent(intent: Intent?) {

        try {
            dao = FSTripDao(applicationContext)
            val extras = intent?.extras
            extras?.let {
                val fromJson = Gson().fromJson(
                    it.getString(TripStatusChangeEnv.WS_BUNDLE_KEY),
                    TripStatusChangeEnv::class.java
                )
                processCreateTrip(fromJson)
            }
            //
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            WBRTripStatusChange.completeWakeFulService(intent)
        }
    }

    private fun processCreateTrip(request: TripStatusChangeEnv) {
        val modelEnv = ApiRequest(
            token = getToken(),
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }
        //
        connectWS<ApiResponse<TripDestinationStatusChangeRec>>(
            url = Constant.WS_TRIP_CHANGE_STATUS,
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
                    response.data?.let { statusChanged ->
                        val transaction = TransactionWsTripDestinationStatusChange(
                            context = applicationContext,
                            FSTripDao(applicationContext),
                            FsTripDestinationDao(applicationContext)
                        )
                        //
                        modelEnv.parameters?.let {
                            if(transaction.save(statusChanged)) {
                                ToolBox.sendBCStatus(
                                    applicationContext,
                                    "CLOSE_ACT",
                                    genericServiceTranslate["generic_process_finalized_msg"],
                                    HMAux(),
                                    "",
                                    "0"
                                )
                            }else{
                                ToolBox.sendBCStatus(
                                    this,
                                    "CUSTOM_ERROR",
                                    genericServiceTranslate["msg_no_data_returned"],
                                    "",
                                    ""
                                )
                            }
                        }?:ToolBox.sendBCStatus(
                            this,
                            "CUSTOM_ERROR",
                            genericServiceTranslate["msg_no_data_returned"],
                            "",
                            ""
                        )
                    } ?: run {
                        if (response.status?.code == 406) {
                            ToolBox.sendBCStatus(
                                this,
                                "CUSTOM_ERROR",
                                response.error_msg,
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
