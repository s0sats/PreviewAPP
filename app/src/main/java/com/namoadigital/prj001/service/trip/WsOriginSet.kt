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
import com.namoadigital.prj001.core.util.TripTokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.core.wsExceptionTreatment
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTripOriginEnv
import com.namoadigital.prj001.model.trip.FSTripOriginRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WsOriginSet : BaseWsIntentService("WsOriginSet", IntentServiceMode.UPLOAD_DATA()) {

    lateinit var dao: FSTripDao
    private val hmAuxTranslate by lazy { loadNetworkTranslate() }


    override fun onHandleIntent(intent: Intent?) {

        try {
            dao = FSTripDao(applicationContext)
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(FSTripOriginEnv.WS_BUNDLE_KEY),
                FSTripOriginEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    private fun execute(request: FSTripOriginEnv) {
        val manager = TripTokenManager().create<FSTripOriginEnv>(this)
        val token = manager.getToken(request)

        val model = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }

        connectWS<ApiResponse<FSTripOriginRec>>(
            url = Constant.WS_TRIP_ORIGIN_SET,
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
                    if(request.originType == null){
                        DatabaseTransactionManager(this).executeTransaction {
                            response.data?.let { data ->
                                dao.updateScn(data.tripPrefix, data.tripCode, data.scn, it)
                                dao.updateOriginDate(
                                    data.tripPrefix,
                                    data.tripCode,
                                    request.originDate,
                                    it
                                )
                            } ?: run {
                                sendBCStatus(WsTypeStatus.ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }
                        }.success {
                            sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                        }.failed {
                            sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }
                        return@watchStatus
                    }

                    DatabaseTransactionManager(this).executeTransaction {
                        response.data?.let { data ->
                            dao.updateScn(data.tripPrefix, data.tripCode, data.scn, it)
                            dao.updateOrigin(
                                data.tripPrefix,
                                data.tripCode,
                                request.originDate,
                                request.originType,
                                request.originSiteCode,
                                request.siteDesc,
                                request.lat,
                                request.lon,
                                it
                            )
                        } ?: run {
                            sendBCStatus(WsTypeStatus.ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }
                    }.success {
                        sendBCStatus(WsTypeStatus.CLOSE_ACT("ok"))
                    }.failed {
                        sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                    }
                }
            )
        }
    }
}