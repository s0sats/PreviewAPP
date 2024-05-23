package com.namoadigital.prj001.service.trip

import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetAvailablesDestinationsUseCase
import com.namoadigital.prj001.core.util.WsTypeStatus
import com.namoadigital.prj001.core.util.sendBCStatus
import com.namoadigital.prj001.receiver.trip.WBR_AvailablesDestinations
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailableEnv
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailableRec
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WsAvailablesDestinations : BaseWsIntentService("WsAvailablesDestinations", IntentServiceMode.UPLOAD_DATA()) {

    private var hmAux_Trans = HMAux()

    override fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            try {
                process(
                    it.getInt(GetAvailablesDestinationsUseCase.SHOW_ONLY_TODAY),
                    it.getInt(GetAvailablesDestinationsUseCase.SHOW_ONLY_PRIORITY),
                    it.getInt(GetAvailablesDestinationsUseCase.HIDE_PREVENTIVE),
                    it.getInt(GetAvailablesDestinationsUseCase.SHOW_ONLY_SITE_WITH_PLANNING),
                    it.getInt(GetAvailablesDestinationsUseCase.SITE_CODE)
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
                WBR_AvailablesDestinations.completeWakeFulService(intent)
            }
        }
    }

    private fun process(
        showOnlyToday: Int,
        showOnlyPriority: Int,
        hidePreventive: Int,
        showOnlySiteWithPlanning: Int,
        siteCode: Int
    ) {

        val gson = GsonBuilder().serializeNulls().create()

        val env = DestinationAvailableEnv(
            parameters = DestinationAvailableEnv.Parameters(
                showOnlyToday,
                showOnlyPriority,
                hidePreventive,
                showOnlySiteWithPlanning,
                if (siteCode == -1) null else siteCode
            )
        )
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT


        val result = ToolBox_Con.connWebService(
            Constant.WS_DESTINATION_AVAILABLE,
            gson.toJson(env)
        )

        sendBCStatus(
            WsTypeStatus.UPDATE_DIALOG_MESSAGE(
                message = genericServiceTranslate["generic_processing_data"],
                required = "0"
            )
        )

        val rec = gson.fromJson(result, DestinationAvailableRec::class.java)


        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                1,
                1
            ) || !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg
            )
        ) {
            return
        }


        ToolBox.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            hmAux_Trans[""],
            HMAux(),
            gson.toJson(rec.data?.list ?: emptyList<DestinationAvailables>()),
            "0"
        )
    }


    companion object {

        val NAME = WsAvailablesDestinations::class.java.name
    }

}