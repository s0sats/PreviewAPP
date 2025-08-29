package com.namoadigital.prj001.service.base

import android.app.IntentService
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.loadNetworkTranslate
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

abstract class BaseWsIntentService(
    private val serviceName:String,
    private val notificationParams : IntentServiceMode): IntentService(serviceName) {

    constructor(serviceName:String):this(serviceName, IntentServiceMode.WS_DATA())

    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    open val genericServiceTranslate: HMAux by lazy {
        loadGenericTranslation()
    }
    open val netwrokServiceTranslate: HMAux by lazy {
        loadNetworkTranslate()
    }

    override fun onCreate() {
        super.onCreate()
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder = ToolBox_Inf.getLowImportanceBuilder(
                applicationContext, nm
            )
            builder.setOngoing(true)
            builder.setContentTitle(applicationContext.getString(R.string.title_notification_generic))
            builder.setContentText(applicationContext.getString(notificationParams.contentText))
            builder.setSmallIcon(notificationParams.smallIcon)
            val notification = builder.build()
            startForeground(notificationParams.notificationID, notification)
        }
    }
    //
    override fun onTimeout(startId: Int, fgsType: Int) {
        super.onTimeout(startId, fgsType)

        Firebase.crashlytics.log("$serviceName onTimeout: ${ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")} ")
        stopSelf(startId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }else {
            stopSelf()
        }
    }
    //
    private fun loadGenericTranslation(): HMAux {
        val translist = listOf<String>(
            "generic_sending_data_msg",
            "generic_receiving_data_msg",
            "generic_processing_data",
            "generic_process_finalized_msg",
            "msg_no_data_returned"
        )
        //
        val mResourceCode = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModuleCode,
            mResourceName
        )
        //
        return ToolBox_Inf.setLanguage(
            applicationContext,
            mModuleCode,
            mResourceCode,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            translist
        )
    }


    sealed class IntentServiceMode(
        open val contentText: Int,
        open val smallIcon: Int,
        open val notificationID: Int,
    ){
        data class UPLOAD_DATA(
            override val contentText: Int = R.string.notification_msg_upload,
            override val smallIcon: Int = R.drawable.upload_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_SYNC_ID

        ): IntentServiceMode(contentText, smallIcon, notificationID)

        data class CUSTOMER_DATA(
            override val contentText: Int = R.string.msg_synchronizing_data,
            override val smallIcon: Int = R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_CUSTOMER

        ): IntentServiceMode(contentText, smallIcon, notificationID)

        data class CUSTOMER_SITE_LICENSE_DATA(
            override val contentText: Int = R.string.msg_synchronizing_data,
            override val smallIcon: Int = R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_CUSTOMER_SITE_LICENSE

        ): IntentServiceMode(contentText, smallIcon, notificationID)

        data class DOWNLOAD_DATA(
            override val contentText: Int = R.string.notification_msg_download,
            override val smallIcon: Int =R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_SYNC_ID
        ): IntentServiceMode(contentText, smallIcon, notificationID)

         data class TICKET_DOWNLOAD_DATA(
            override val contentText: Int = R.string.notification_msg_download,
            override val smallIcon: Int =R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_TICKET_DOWNLOAD
        ): IntentServiceMode(contentText, smallIcon, notificationID)

        data class SYNC_SO_DATA(
            override val contentText: Int = R.string.notification_msg_download,
            override val smallIcon: Int =R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_SO_ID
        ): IntentServiceMode(contentText, smallIcon, notificationID)

        data class WS_DATA(
            override val contentText: Int = R.string.notification_msg_download,
            override val smallIcon: Int =R.drawable.download_animation,
            override val notificationID: Int = ConstantBaseApp.NOTIFICATION_WS_ID
        ): IntentServiceMode(contentText, smallIcon, notificationID)

    }
}