package com.namoadigital.prj001.extensions

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.namoadigital.prj001.R
import com.namoadigital.prj001.ui.act001.Act001_Main
import com.namoadigital.prj001.util.ConstantBaseApp.NOTIFICATION_BOOT_COMPLET_ID
import com.namoadigital.prj001.util.ToolBox_Inf

fun generatePendingIntent(context: Context) {
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val mIntent = Intent(context, Act001_Main::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pi = PendingIntent.getActivity(
        context,
        0,
        mIntent,
        ToolBox_Inf.getMutableFlag(PendingIntent.FLAG_UPDATE_CURRENT, true)
    )

    val builder = ToolBox_Inf.getHighImportanceNotificationBuilder(context, nm).apply {
        setSmallIcon(R.mipmap.ic_namoa)
        setAutoCancel(true)
        setContentTitle(context.getString(R.string.location_pendency_ttl))
        setContentText(context.getString(R.string.location_pendency_msg))
        setOngoing(true)
        setContentIntent(pi)
    }

    nm.notify(NOTIFICATION_BOOT_COMPLET_ID, builder.build())
}