package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.Gson
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.NETWORK_GENERIC_ERROR
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
import com.namoadigital.prj001.model.trip.TripFleetSetEnv
import com.namoadigital.prj001.model.trip.TripFleetSetRec
import com.namoadigital.prj001.model.trip.TripTarget
import com.namoadigital.prj001.model.trip.toTripTarget
import com.namoadigital.prj001.receiver.trip.WBRFleetSet
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class WSFleetSet : BaseWsIntentService("WSFleetSet", IntentServiceMode.UPLOAD_DATA()){


    lateinit var dao: FSTripDao
    lateinit var destinationDao: FsTripDestinationDao
    private  val hmAuxTranslate by lazy { loadNetworkTranslate() }

    override fun onHandleIntent(intent: Intent?) {

        try {
            dao = FSTripDao(applicationContext)
            destinationDao = FsTripDestinationDao(applicationContext)
            val extras = intent?.extras
            val request =  Gson().fromJson(
                extras?.getString(TripFleetSetEnv.WS_BUNDLE_KEY),
                TripFleetSetEnv::class.java
            )
            execute(request)
        } catch (e: Exception) {
            wsExceptionTreatment(e)
        } finally {
            ToolBox_Inf.callPendencyNotification(applicationContext)
            WBRFleetSet.completeWakeFulService(intent)
        }
    }

    private fun execute(request: TripFleetSetEnv) {
        val deletePhoto = request.deletePhoto && !request.imageKey.isNullOrBlank()

        val manager = TokenManager<TripFleetSetEnv>(applicationContext)
        val token = manager.getToken(request)
        val modelEnv = ApiRequest(
            token = token,
            parameters = request.copy(
                imageKey = if(deletePhoto) null else request.imageKey,
                imageChanged = if(deletePhoto) 1 else request.imageChanged
            )
        ).apply {
            session_app = getUserSessionAPP()
        }


        connectWS<ApiResponse<TripFleetSetRec>>(
            url = Constant.WS_TRIP_FLEET_SET,
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
                    response.data?.let { data ->
                        DatabaseTransactionManager(this).executeTransaction { db ->
                            dao.updateScn(data.tripPrefix, data.tripCode, data.scn, db)
                            val image = if(!request.deletePhoto){
                                request.imageKey
                            }else {
                                ""
                            }
                            when(request.target.toTripTarget()){
                                TripTarget.DESTINATION -> {
                                    destinationDao.updateArrivedFleet(
                                        tripPrefix = data.tripPrefix,
                                        tripCode = data.tripCode,
                                        destinationSeq = request.destinationSeq,
                                        odometer = request.odometer,
                                        photoPath = image,
                                        photoChanged = request.imageChanged,
                                        db = db
                                    )
                                }
                                else -> {
                                    dao.updateFleet(
                                        data.tripPrefix,
                                        data.tripCode,
                                        request.licensePlate,
                                        request.odometer,
                                        request.target.toTripTarget(),
                                        image,
                                        request.photoChanged,
                                        db
                                    )
                                }
                            }
                        }.success {
                            if(deletePhoto){
                                try {
                                    val file =
                                        File("${ConstantBase.CACHE_PATH_PHOTO}/${request.imageKey}")
                                    if (file.exists()) {
                                        file.delete()
                                    }
                                }catch (exception:Exception){
                                    ToolBox.registerException(
                                        javaClass.name,
                                        exception
                                    )
                                    exception.printStackTrace()
                                }
                            }
                            sendBCStatus(WsTypeStatus.CLOSE_ACT(response = "ok"))
                        }.failed {
                            sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[DB_TRANSACTION_ERROR_LBL]))
                        }
                    } ?: run {
                        sendBCStatus(WsTypeStatus.CUSTOM_ERROR(hmAuxTranslate[NETWORK_GENERIC_ERROR]))
                    }
                }
            )

        }


    }


}