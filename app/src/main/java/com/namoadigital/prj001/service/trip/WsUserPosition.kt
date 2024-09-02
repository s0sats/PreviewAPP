package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.domain.model.UserPositionEnv
import com.namoadigital.prj001.core.trip.domain.model.UserPositionRec
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.extensions.getToken
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FsTripPosition
import com.namoadigital.prj001.model.trip.mapping.toEnv
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WsUserPosition : BaseWsIntentService("WsUserPosition", IntentServiceMode.UPLOAD_DATA()) {

    lateinit var dao: FsTripPositionDao

    @Inject
    lateinit var pref: CurrentTripPref
    override fun onHandleIntent(intent: Intent?) {
        try {
            dao = FsTripPositionDao(this)
            val extras = intent?.extras
            val isCloudFlow = extras?.getBoolean(CLOUD_FLOW, false) ?: false
            restartPreferencesCounters(isCloudFlow)
            process(isCloudFlow)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    private fun restartPreferencesCounters(isCloudFlow: Boolean) {
        if (!isCloudFlow) {
            val model = pref.read()
            model.transmission_date = ToolBox.sDTFormat_Agora(FULL_TIMESTAMP_TZ_FORMAT)
            model.transmission_counter = 0
            model.position_counter = 0
            pref.write(model)
            dao.setLastPositionAsReference()
        }
    }

    fun process(isCloudFlow: Boolean) {

        val positionItems = mutableListOf<FsTripPosition>()
        var checkNewData = false

        positionItems.addAll(dao.getAllUpdateRequiredWithToken())
        val modelEnv = if (positionItems.isNotEmpty()) {
            checkNewData = true
            createModelEnv(positionItems[0].token.orEmpty(), positionItems.map { it.toEnv() })
        } else {
            positionItems.apply {
                clear()
                addAll(dao.getAllUpdateRequiredTokenNull())
            }
            val newToken = getToken()
            positionItems.map {
                dao.updateToken(
                    it.customerCode,
                    it.tripPrefix,
                    it.tripCode,
                    it.tripPositionSeq,
                    newToken
                )
            }
            createModelEnv(newToken, positionItems.map { it.toEnv() })
        }

        if (positionItems.isNotEmpty()) {
            connectWS<ApiResponse<UserPositionRec>>(
                url = Constant.WS_TRIP_USER_POSITION,
                model = modelEnv,
                errorFeedback = false
            ) {
                it.watchStatus(
                    success = { _ ->

                        if (positionItems.isNotEmpty()) {
                            positionItems.map { position ->
                                dao.updateRequired(
                                    position.customerCode,
                                    position.tripPrefix,
                                    position.tripCode,
                                    position.tripPositionSeq,
                                    0
                                )
                            }
                        }
                        dao.delete()
                        val model = pref.read()
                        model.isRef = 0
                        model.position_counter = 0
                        pref.write(model)
                        //
                        checkResults(checkNewData, isCloudFlow)
                        //
                    },
                    error = { _, _->
                        if (positionItems.isNotEmpty()) {
                            positionItems.map { position ->
                                dao.updateRequired(
                                    position.customerCode,
                                    position.tripPrefix,
                                    position.tripCode,
                                    position.tripPositionSeq,
                                    0
                                )
                            }
                        }
                        dao.delete()
                        checkResults(checkNewData, isCloudFlow)
                    },
                    failed = {
                        sendBCStatus(WsTypeStatus.FCMStatus(UPDATE_CLOUD))
                    }
                )
            }
        }else{
            if (isCloudFlow) {
                sendBCStatus(WsTypeStatus.CLOSE_ACT("OK"))
            }
        }
    }

    private fun checkResults(checkNewData: Boolean, isCloudFlow: Boolean) {
        if (checkNewData) {
            process(isCloudFlow)
        }else{
            if (isCloudFlow) {
                sendBCStatus(WsTypeStatus.CLOSE_ACT("ERROR"))
            } else {
                sendBCStatus(WsTypeStatus.FCMStatus(UPDATE_CLOUD))
            }
        }
    }

    private fun createModelEnv(
        token: String,
        parameters: List<UserPositionEnv>
    ): ApiRequest<List<UserPositionEnv>> {
        return ApiRequest(
            token = token,
            parameters = parameters
        ).apply {
            session_app = getUserSessionAPP()
        }
    }

    companion object {
        val NAME: String = WsUserPosition::class.java.name
        var CLOUD_FLOW = "cloud_flow"
        var UPDATE_CLOUD = "update_cloud"
    }
}