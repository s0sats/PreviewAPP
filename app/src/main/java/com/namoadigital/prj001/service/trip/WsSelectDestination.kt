package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.connectWS
import com.namoadigital.prj001.core.data.remote.domain.ApiRequest
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SaveDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SelectDestinationUseCase
import com.namoadigital.prj001.core.util.TokenManager
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.getUserSessionAPP
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.receiver.trip.WBR_SelectDestination
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationEnv
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WsSelectDestination :
    BaseWsIntentService("WsSelectDestionation", IntentServiceMode.DOWNLOAD_DATA()) {

    private var hmAux_Trans = HMAux()

    @Inject
    lateinit var saveDestinationUseCase: SaveDestinationUseCase

    @Inject
    lateinit var tripDao: FSTripDao

    override fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            try {
                process(
                    it.getString(SelectDestinationUseCase.DESTINATION_TYPE, ""),
                    it.getInt(SelectDestinationUseCase.TRIP_PREFIX),
                    it.getInt(SelectDestinationUseCase.TRIP_CODE),
                    it.getInt(SelectDestinationUseCase.TRIP_SCN),
                    if (it.containsKey(SelectDestinationUseCase.SITE_CODE)) it.getInt(
                        SelectDestinationUseCase.SITE_CODE
                    ) else null,
                    if (it.containsKey(SelectDestinationUseCase.DESTINATION_TICKET_PREFIX)) it.getInt(
                        SelectDestinationUseCase.DESTINATION_TICKET_PREFIX
                    ) else null,
                    if (it.containsKey(SelectDestinationUseCase.DESTINATION_TICKET_CODE)) it.getInt(
                        SelectDestinationUseCase.DESTINATION_TICKET_CODE
                    ) else null,
                    if (it.containsKey(SelectDestinationUseCase.CURRENT_LAT)) it.getDouble(
                        SelectDestinationUseCase.CURRENT_LAT
                    ) else null,
                    if (it.containsKey(SelectDestinationUseCase.CURRENT_LON)) it.getDouble(
                        SelectDestinationUseCase.CURRENT_LON
                    ) else null,
                )
            } catch (e: Exception) {
                ToolBox_Inf.wsExceptionTreatment(applicationContext, e).let { string ->
                    ToolBox_Inf.registerException(javaClass.name, e)
                    ToolBox.sendBCStatus(
                        applicationContext,
                        "ERROR_1",
                        string.toString(),
                        "",
                        ""
                    )
                }
            } finally {
                ToolBox_Inf.callPendencyNotification(applicationContext)
                WBR_SelectDestination.completeWakeFulService(intent)
            }
        }
    }

    private fun process(
        destinationType: String,
        tripPrefix: Int,
        tripCode: Int,
        scn: Int,
        siteCode: Int?,
        destinationTicketPrefix: Int?,
        destinationTicketCode: Int?,
        currentLat: Double?,
        currentLon: Double?,
    ) {

        val gson = GsonBuilder().serializeNulls().create()

        val params = SelectDestinationEnv(
            tripPrefix = tripPrefix,
            tripCode = tripCode,
            scn = scn,
            destinationType = destinationType,
            siteCode = siteCode,
            destinationTicketPrefix = destinationTicketPrefix,
            destinationTicketCode = destinationTicketCode,
            currentLat = currentLat,
            currentLon = currentLon,
        )
        //
        val manager = TokenManager<SelectDestinationEnv>(applicationContext)
        val token = manager.getToken(params)
        val modelEnv = ApiRequest(
            token = token,
            parameters = params
        ).apply {
            session_app = getUserSessionAPP()
        }
        //
        connectWS<ApiResponse<SelectDestinationRec>>(
            url = Constant.WS_SELECT_DESTINATION,
            model = modelEnv
        ) {
            it.watchStatus(
                success = { response ->
                    manager.deleteToken()
                    sendBCStatus(
                        WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                            message = genericServiceTranslate["generic_processing_data"],
                            required = "0"
                        )
                    )
                    response.data?.let { data ->
                        ToolBox.sendBCStatus(
                            applicationContext,
                            "CLOSE_ACT",
                            hmAux_Trans[""],
                            HMAux(),
                            gson.toJson(data),
                            "0"
                        )
                    }
                },
                failed = {

                    val trip = tripDao.getTrip()
                    trip?.let { trip ->
                            //
                            ToolBox.sendBCStatus(
                                applicationContext,
                                "ERROR_1",
                                hmAux_Trans[""],
                                HMAux(),
                                "",
                                "0"
                            )
                    }
                }
            )
        }
    }


    companion object {

        val NAME = WsSelectDestination::class.java.name
    }

}