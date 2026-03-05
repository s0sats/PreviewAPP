package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.Gson
import com.namoadigital.prj001.core.DB_GET_DATA_ERROR_LBL
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.NETWORK_GENERIC_ERROR
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.core.util.TripTokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTripFull
import com.namoadigital.prj001.model.trip.FSTripFullUpdateEnv
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WsSendTripUpdate : BaseWsIntentService("WsSendTripUpdate", IntentServiceMode.UPLOAD_DATA()) {

    private var reSend:Boolean = false
    @Inject lateinit var tripRepository:TripRepository

    override fun onHandleIntent(intent: Intent?) {
        try {
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(FSTripFullUpdateEnv::class.java.name),
                FSTripFullUpdateEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    fun execute(request: FSTripFullUpdateEnv) {
        val manager = TripTokenManager().create<FSTripFullUpdateEnv>(this)
        val token = manager.getToken(request, true)
        val requestToken = if(token.isNotBlank()){
            reSend = true
            token
        }else{
            reSend = false
            manager.getToken(request)
        }
        //
        val model = ApiRequest(
            token = requestToken,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }
        //
        connectWS<ApiResponse<FSTripFull>>(
            url = Constant.WS_TRIP_UPDATE,
            model = model
        ) {

            it.watchStatus(
                success = { response ->
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    //
                    val tripDao = FSTripDao(this)
                    //
                    response.data?.tripPrefix?.let { tripPrefix ->
                        tripDao.updateScn(tripPrefix, response.data!!.tripCode!!, response.data!!.scn!!)
                        manager.deleteToken()
                        tripDao.getTrip()?.let{ trip ->
                            if(reSend) {
                                val requestUpdated = tripRepository.getTripFullUpdateEnv(trip)
                                if(requestUpdated != null) {
                                    execute(requestUpdated)
                                }else{
                                    sendBCStatus(WsTypeStatus.CUSTOM_ERROR(message = netwrokServiceTranslate[DB_GET_DATA_ERROR_LBL]))
                                }
                            }
                        }?: run {
                            sendBCStatus(WsTypeStatus.CUSTOM_ERROR(netwrokServiceTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }
                    }?:run {
                        response.data?.tripFull?.let { tripFull ->
                            if(reSend){
                                tripDao.updateScn(tripFull.tripPrefix, tripFull.tripCode, tripFull.scn)
                                manager.deleteToken()
                                tripDao.getTripByPk(tripFull.customerCode, tripFull.tripPrefix, tripFull.tripCode)?.let { trip ->
                                    val requestUpdated = tripRepository.getTripFullUpdateEnv(trip)
                                    if(requestUpdated != null) {
                                        execute(requestUpdated)
                                    }else{
                                        sendBCStatus(WsTypeStatus.CUSTOM_ERROR(message = netwrokServiceTranslate[DB_GET_DATA_ERROR_LBL]))
                                    }
                                }
                            } else {
                                tripDao.syncTripFull(response.data?.tripFull!!).let { daoReturn ->
                                    if (!daoReturn.hasError()) {
                                        manager.deleteToken()
                                        sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                                    } else {
                                        tripDao.setSyncRequired()
                                        sendBCStatus(WsTypeStatus.CUSTOM_ERROR(message = netwrokServiceTranslate[DB_TRANSACTION_ERROR_LBL]))
                                    }
                                }
                            }
                        }
                    }
                },
                failed = {
                    sendBCStatus(WsTypeStatus.ERROR(message = netwrokServiceTranslate[NETWORK_GENERIC_ERROR]))
                }
            )

        }
    }

}