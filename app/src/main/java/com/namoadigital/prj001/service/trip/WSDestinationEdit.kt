package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.Gson
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.loadNetworkTranslate
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.TripDestinationEditEnv
import com.namoadigital.prj001.model.trip.TripDestinationEditRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WSDestinationEdit :
    BaseWsIntentService("WSDestinationEdit", IntentServiceMode.UPLOAD_DATA()) {

    lateinit var dao: FSTripDao
    lateinit var destinationDao: FsTripDestinationDao
    private val hmAuxTranslate by lazy { loadNetworkTranslate(this) }


    override fun onHandleIntent(intent: Intent?) {
        try {
            dao = FSTripDao(applicationContext)
            destinationDao = FsTripDestinationDao(applicationContext)
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(TripDestinationEditEnv.WS_BUNDLE_KEY),
                TripDestinationEditEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    private fun execute(request: TripDestinationEditEnv) {
        val manager = TokenManager<TripDestinationEditEnv>(applicationContext)
        val token = manager.getToken(request)
        val modelEnv = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }


        connectWS<ApiResponse<TripDestinationEditRec>>(
            url = Constant.WS_TRIP_DESTINATION_EDIT,
            model = modelEnv
        ) {
            it.watchStatus(
                success = { response ->
                    //
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    //
                    response.data?.let { data ->
                        DatabaseTransactionManager(this).executeTransaction { db ->
                            dao.updateScn(
                                data.tripPrefix,
                                data.tripCode,
                                data.scn,
                                db
                            )

                            destinationDao.updateArrivedAndDeparted(
                                request.tripPrefix,
                                request.tripCode,
                                request.destinationSeq,
                                request.arrivedDate,
                                request.departedDate,
                                db
                            )
                        }.success {
                            sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                        }.failed {
                            sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }
                    }
                }
            )
        }
    }
}