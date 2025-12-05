package com.namoadigital.prj001.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.extensions.generatePendingIntent
import com.namoadigital.prj001.extensions.hasBigFileProcessActive
import com.namoadigital.prj001.extensions.isAppInForeground
import com.namoadigital.prj001.service.SV_LocationTracker
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.worker.big_file.utils.BigFileManager

class WBR_BootCompleted : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            activateLocationService(context)
        }
    }

    private fun activateLocationService(context: Context) {
        if (ToolBox_Inf.isUsrAppLogged(context)) {
            val dao = FSTripDao(context)
            val trip = dao.getTrip()
            val pendencies = ToolBox_Inf.getLocationPendencies(context)
            if((!SV_LocationTracker.status && pendencies > 0) || trip != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    if (!context.isAppInForeground()) {
                        generatePendingIntent(context)
                    }
                }else{
                    if (!SV_LocationTracker.status) {
                        val pendencies = ToolBox_Inf.getLocationPendencies(context)
                        if (pendencies > 0) {
                            ToolBox_Inf.call_Location_Tracker_On_Background(
                                context,
                                SV_LocationTracker.LOCATION_BACKGROUND
                            )
                        }
                    }
                }
            }
            //
            if (context.hasBigFileProcessActive()) {
                val bigFileManager = BigFileManager(context)
                bigFileManager.getWorkCheckBigFileRequest()
            }
        }
    }
}