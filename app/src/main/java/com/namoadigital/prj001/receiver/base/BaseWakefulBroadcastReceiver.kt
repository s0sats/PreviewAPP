package com.namoadigital.prj001.receiver.base

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.legacy.content.WakefulBroadcastReceiver

abstract class BaseWakefulBroadcastReceiver<T : Any> constructor(
    private val intentClass: Class<T>
) : WakefulBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val extras = intent?.extras

        val newIntent = if (extras != null) {
            Intent(context, intentClass).apply {
                this.putExtras(extras)
            }
        } else {
            Intent(context, intentClass)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(newIntent)
        } else {
            WakefulBroadcastReceiver.startWakefulService(context, newIntent)
        }
    }

    companion object {
        fun completeWakeFulService(intent: Intent?) =
            WakefulBroadcastReceiver.completeWakefulIntent(intent)
    }
}
