package com.namoadigital.prj001.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.namoadigital.prj001.extensions.isGpsEnabled

class GpsStateReceiver : BroadcastReceiver() {

    var onGpsState: ((Boolean) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (LocationManager.PROVIDERS_CHANGED_ACTION  == intent.action) {
            val isGpsEnabled = context.isGpsEnabled()
            onGpsState?.invoke(isGpsEnabled)
        }
    }
}

