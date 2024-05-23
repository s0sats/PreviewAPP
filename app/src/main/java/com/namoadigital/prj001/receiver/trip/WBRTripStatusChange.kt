package com.namoadigital.prj001.receiver.trip

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.legacy.content.WakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WSTripStatusChange

class WBRTripStatusChange : WakefulBroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        val extras = intent?.extras
        Intent(context, WSTripStatusChange::class.java).apply {
            this.putExtras(extras ?: Bundle())
            //
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                startWakefulService(context, this)
            }
        }
    }

    companion object {
        fun completeWakeFulService(intent: Intent?) = completeWakefulIntent(intent)
    }
}