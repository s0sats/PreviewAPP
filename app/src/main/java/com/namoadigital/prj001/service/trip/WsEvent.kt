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
import com.namoadigital.prj001.dao.trip.FSTripEventDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripEventEnv
import com.namoadigital.prj001.model.trip.FSTripEventRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class   WsEvent : BaseWsIntentService("WsEvent", IntentServiceMode.UPLOAD_DATA()) {


    private lateinit var tripDao: FSTripDao
    private lateinit var dao: FSTripEventDao
    private val hmAuxTranslate by lazy { loadNetworkTranslate() }

    override fun onHandleIntent(intent: Intent?) {
        try {
            tripDao = FSTripDao(this)
            dao = FSTripEventDao(this)
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(FSTripEventEnv.WS_BUNDLE_KEY),
                FSTripEventEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    private fun execute(request: FSTripEventEnv) {
        val manager = TokenManager<FSTripEventEnv>(this)
        val token = manager.getToken(request)

        val model = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }

        connectWS<ApiResponse<FSTripEventRec>>(
            url = Constant.WS_TRIP_EVENT,
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
                    response.data?.let { data ->
                        DatabaseTransactionManager(this).executeTransaction { db ->
                            tripDao.updateScn(data.tripPrefix, data.tripCode, data.scn, db)

                            val getEvent = dao.getEventFull(
                                data.tripPrefix,
                                data.tripCode,
                                data.eventSeq,
                                db
                            )
                            getEvent?.let { tripEvent ->
                                tripEvent.apply {
                                    cost = request.eventCost
                                    comment = request.eventComments
                                    eventSeq = data.eventSeq
                                    eventStart = request.eventStart
                                    eventEnd = request.eventEnd
                                    eventStatus = request.eventStatus
                                    photoLocal = request.eventPhoto
                                    eventTypeCode = request.eventTypeCode
                                    eventTypeDesc = request.eventTypeDesc
                                    eventPhotoChanged = request.changedPhoto
                                }
                                dao.update(tripEvent, db)

                            } ?: dao.update(
                                FSTripEvent(
                                    customerCode = getCustomerCode(),
                                    tripPrefix = data.tripPrefix,
                                    tripCode = data.tripCode,
                                    eventTypeCode = request.eventTypeCode,
                                    eventSeq = data.eventSeq,
                                    eventTypeDesc = request.eventTypeDesc,
                                    eventStatus = request.eventStatus,
                                    eventTimeAlert = null,
                                    eventAllowedTime = null,
                                    eventTime = null,
                                    cost = request.eventCost,
                                    comment = request.eventComments,
                                    photoLocal = request.eventPhoto,
                                    photoName = null,
                                    photoUrl = null,
                                    eventStart = request.eventStart ?: "",
                                    eventEnd = request.eventEnd ?: "",
                                    eventPhotoChanged = request.changedPhoto,
                                    destinationSeq =  request.destinationSeq,
                                ), db
                            )
                        }.success {
                            sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                        }.failed {
                            sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }

                    } ?: run {
                        sendBCStatus(WsTypeStatus.ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                    }
                }
            )
        }
    }
}