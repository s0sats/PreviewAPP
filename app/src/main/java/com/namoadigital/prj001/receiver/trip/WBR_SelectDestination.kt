package com.namoadigital.prj001.receiver.trip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.legacy.content.WakefulBroadcastReceiver
import com.namoadigital.prj001.service.trip.WsSelectDestination

class WBR_SelectDestination : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val extras = intent?.extras
        Intent(context, WsSelectDestination::class.java).apply {
            extras?.let { bundle -> putExtras(bundle) } ?: putExtras(Bundle())
            startWakefulService(context, this)
        }

    }

    companion object {
        fun completeWakeFulService(intent: Intent?) = completeWakefulIntent(intent)
    }
}