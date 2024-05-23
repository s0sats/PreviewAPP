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
import com.namoadigital.prj001.extensions.getToken
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.TripNewEnv
import com.namoadigital.prj001.model.trip.TripNewRec
import com.namoadigital.prj001.receiver.trip.WBR_CreateTrip
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WsCreateTrip : BaseWsIntentService("WsCreateTrip", IntentServiceMode.UPLOAD_DATA()) {

    lateinit var dao: FSTripDao

    override fun onHandleIntent(intent: Intent?) {

        try {
            dao = FSTripDao(this)
            val extras = intent?.extras
            val request = extras?.let {
                val fromJson = Gson().fromJson(
                    it.getString(TripNewEnv.WS_BUNDLE_KEY),
                    TripNewEnv::class.java
                )
                fromJson

            } ?: TripNewEnv(null, null)
            //
            processCreateTrip(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            WBR_CreateTrip.completeWakeFulService(intent)
        }
    }

    private fun processCreateTrip(request: TripNewEnv) {
        val modelEnv = ApiRequest(
            token = getToken(),
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }
        //
        connectWS<ApiResponse<TripNewRec>>(
            url = Constant.WS_TRIP_NEW,
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
                    response.data?.let { trip ->
                        dao.addUpdate(
                            FSTrip(
                                customerCode = ToolBox_Con.getPreference_Customer_Code(
                                    applicationContext
                                ),
                                tripPrefix = trip.tripPrefix,
                                tripCode = trip.tripCode,
                                scn = trip.scn,
                                originDate = trip.originDate,
                                tripStatus = trip.tripStatus,
                                originType = trip.originType,
                                originSiteCode = trip.originSiteCode,
                                originSiteDesc = trip.originSiteDesc,
                                originLat = trip.lat,
                                originLon = trip.lon,
                                positionDistanceMin = trip.positionDistanceMin,
                                requireFleetData = trip.requireFleetData ?: 0,
                                requireDestinationFleetData = trip.requireDestinationFleetData ?: 0,
                                tripUserCode = ToolBox_Con.getPreference_User_Code(
                                    applicationContext
                                )
                                    .toInt(),
                                tripUserName = ToolBox_Con.getPreference_User_Code_Nick(
                                    applicationContext
                                )!!,
                                distanceRefMinutes = trip.distanceRefMinutes,
                                distanceRefMinutesTrans = trip.distanceRefMinutesTrans,
                            )
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

    private fun getTripNewModelEnv(lat: Double?, lon: Double?): TripNewEnv {
        return TripNewEnv(
            lat,
            lon
        )
    }


    companion object {
        const val LAT = "LAT"
        const val LON = "LON"
    }


}
