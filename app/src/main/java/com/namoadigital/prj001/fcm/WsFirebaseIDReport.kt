package com.namoadigital.prj001.fcm

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.model.TGoogle_Env
import com.namoadigital.prj001.model.TGoogle_Rec
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WsFirebaseIDReport(val context: Context) {

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    //
    private var fcmJob: Job? = null

    //
    fun sendGcmId(gcmResult: () -> Unit) {
        if (fcmJob?.isActive == true) {
            gcmResult()
            return
        }
        //
        fcmJob = serviceScope.launch {
            var sToken:String
            try {
                sToken = FirebaseMessaging.getInstance().getToken().await()

                if (!ToolBox_Con.getPreference_Google_ID(context)
                        .equals(sToken, ignoreCase = true)
                    || !ToolBox_Con.getPreference_Google_ID_OK(context)
                        .equals(ConstantBaseApp.MAIN_RESULT_OK, ignoreCase = true)
                ) {
                    //
                    ToolBox_Con.setPreference_Google_ID(
                        context,
                        sToken
                    )

                    //
                    ToolBox_Con.setPreference_Google_ID_OK(
                        context,
                        ""
                    )
                }
            } catch (e: Exception) {
                sToken=""
                ToolBox_Inf.registerException(javaClass.name, e)
                ToolBox_Inf.scheduleFirebaseRegistrationWork(context)
            }

            if (!ToolBox_Con.getPreference_Session_App(context)
                    .equals("", ignoreCase = true)
                && !ToolBox_Con.getPreference_Google_ID(context)
                    .equals("", ignoreCase = true)
                && !ToolBox_Con.getPreference_Google_ID(context)
                    .equals(sToken, ignoreCase = true)
                && ToolBox_Con.getPreference_Google_ID_OK(context)
                    .equals("", ignoreCase = true)
            ) {
                val env = TGoogle_Env()
                env.setApp_code(Constant.PRJ001_CODE)
                env.setApp_version(Constant.PRJ001_VERSION)

                env.setSession_app(
                    ToolBox_Con.getPreference_Session_App(context)
                )
                env.setApp_type(Constant.PKG_APP_TYPE_DEFAULT)
                env.setGcm_id(
                    ToolBox_Con.getPreference_Google_ID(context)
                )

                val gson = GsonBuilder().serializeNulls().create()
                try {
                    val sResults = ToolBox_Con.connWebService(
                        Constant.WS_GOOGLE,
                        gson.toJson(env)
                    )

                    val rec = gson.fromJson(
                        sResults,
                        TGoogle_Rec::class.java
                    )

                    if (rec.getRet() != null && ConstantBaseApp.MAIN_RESULT_OK.equals(
                            rec.getRet(),
                            ignoreCase = true
                        )
                    ) {
                        ToolBox_Con.setPreference_Google_ID_OK(
                            context,
                            ConstantBaseApp.MAIN_RESULT_OK
                        )

                    }
                } catch (e: Exception) {
                    ToolBox_Inf.registerException(javaClass.name, e)
                    ToolBox_Inf.scheduleFirebaseID_ReportWork(context)
                }
            }
        }
        //
        fcmJob?.invokeOnCompletion {
            gcmResult()
        } ?: run {
            gcmResult()
        }
    }
}