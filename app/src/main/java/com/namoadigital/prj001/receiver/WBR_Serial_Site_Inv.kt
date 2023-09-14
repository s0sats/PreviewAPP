package com.namoadigital.prj001.receiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.legacy.content.WakefulBroadcastReceiver
import com.namoadigital.prj001.service.WsSerialSiteInventory

class WBR_Serial_Site_Inv : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intents = intent?.extras
        Intent(context, WsSerialSiteInventory::class.java).apply {
            intents?.let { bundle ->
                this.putExtras(bundle)
            } ?: this.putExtras(Bundle())

            startWakefulService(context, this)
        }

    }

    companion object {
        fun completeWakeFulService(intent: Intent?) = completeWakefulIntent(intent)
    }
}