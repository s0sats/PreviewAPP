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
import com.namoadigital.prj001.dao.trip.FSTripUserDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.trip.TripUserSaveEnv
import com.namoadigital.prj001.model.trip.TripUserSaveRec
import com.namoadigital.prj001.receiver.base.BaseWakefulBroadcastReceiver
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf

class WsTripEditUsers : BaseWsIntentService("WsTripEditUsers", IntentServiceMode.UPLOAD_DATA()) {

    private lateinit var dao: FSTripUserDao
    private lateinit var tripDao: FSTripDao
    private val hmAuxTranslate by lazy { loadNetworkTranslate() }

    override fun onHandleIntent(intent: Intent?) {
        try {
            dao = FSTripUserDao(this)
            tripDao = FSTripDao(this)
            val extras = intent?.extras
            val request = Gson().fromJson(
                extras?.getString(TripUserSaveEnv.WS_BUNDLE_KEY),
                TripUserSaveEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            BaseWakefulBroadcastReceiver.completeWakeFulService(intent)
        }
    }

    private fun execute(request: TripUserSaveEnv) {
        val manager = TripTokenManager().create<TripUserSaveEnv>(this)
        val token = manager.getToken(request)

        val model = ApiRequest(
            token = token,
            parameters = request
        ).apply {
            session_app = getUserSessionAPP()
        }

        connectWS<ApiResponse<TripUserSaveRec>>(
            url = Constant.WS_TRIP_SAVE_USER,
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
                        if (UserAction.valueOf(request.action) == UserAction.EDIT) {
                            DatabaseTransactionManager(this).executeTransaction(
                                block = { db ->
                                    tripDao.updateScn(
                                        request.tripPrefix,
                                        request.tripCode,
                                        data.scn,
                                        db
                                    )
                                    dao.updateUser(
                                        customerCode = getCustomerCode(),
                                        tripPrefix = request.tripPrefix,
                                        tripCode = request.tripCode,
                                        userCode = request.userCode,
                                        userSeq = data.userSeq,
                                        userName = request.userName,
                                        dateStart = request.inDate,
                                        dateEnd = request.outDate,
                                        db
                                    )
                                },
                            ).success {
                                sendBCStatus(WsTypeStatus.CLOSE_ACT(request.userName))
                            }.failed {
                                sendBCStatus(WsTypeStatus.ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }

                        } else {
                            DatabaseTransactionManager(this).executeTransaction(
                                block = { db ->
                                    tripDao.updateScn(
                                        request.tripPrefix,
                                        request.tripCode,
                                        data.scn,
                                        db
                                    )
                                    dao.removeUser(
                                        request.tripPrefix,
                                        request.tripCode,
                                        request.userSeq!!,
                                        db
                                    )
                                }
                            ).success {
                                sendBCStatus(WsTypeStatus.CLOSE_ACT(""))
                            }.failed {
                                sendBCStatus(WsTypeStatus.ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                            }
                        }
                    }
                }
            )
        }
    }
}