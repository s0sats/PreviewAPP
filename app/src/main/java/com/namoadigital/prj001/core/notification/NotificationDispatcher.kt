package com.namoadigital.prj001.core.notification

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission

object NotificationDispatcher {

    @SuppressLint("MissingPermission")
    fun dispatch(context: Context, notification: PushNotification) {
        if(!hasNotificationPermission(context)) return

        val builder = NotificationCompat.Builder(context, notification.channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setSmallIcon(notification.smallIconRes)
            .setAutoCancel(notification.autoCancel)
            .setOngoing(notification.ongoing)
            .setSilent(notification.silent)
            .setPriority(notification.priority.toCompat())
            .setVisibility(notification.visibility.toCompat())

        notification.largeIcon?.let { builder.setLargeIcon(it) }
        notification.color?.let { builder.setColor(it) }
        notification.contentIntent?.let { builder.setContentIntent(it) }
        notification.deleteIntent?.let { builder.setDeleteIntent(it) }
        notification.style?.let { builder.setStyle(it) }
        notification.group?.let { builder.setGroup(it) }
        notification.badgeNumber?.let { builder.setNumber(it) }
        notification.timeoutAfterMs?.let { builder.setTimeoutAfter(it) }
        notification.category?.let { builder.setCategory(it) }
        notification.vibrationPattern?.let { builder.setVibrate(it) }
        notification.lights?.let { (c, on, off) -> builder.setLights(c, on, off) }

        if (notification.groupSummary) builder.setGroupSummary(true)

        notification.actions.forEach { builder.addAction(it) }

        if (notification.progressMax != null && notification.progressCurrent != null) {
            builder.setProgress(
                notification.progressMax,
                notification.progressCurrent,
                notification.progressIndeterminate
            )
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notification.tag, notification.id, builder.build())
        }
    }

    /** Cancela uma notificação pelo ID */
    fun cancel(context: Context, id: Int) {
        if(!hasNotificationPermission(context)) return
        NotificationManagerCompat.from(context).cancel(id)
    }

    /** Cancela uma notificação pelo ID + tag */
    fun cancel(context: Context, tag: String, id: Int) {
        NotificationManagerCompat.from(context).cancel(tag, id)
    }

    /** Cancela todas as notificações */
    fun cancelAll(context: Context) {
        NotificationManagerCompat.from(context).cancelAll()
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else { true }
    }
}
