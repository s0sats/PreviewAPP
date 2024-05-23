package com.namoadigital.prj001.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.namoadigital.prj001.extensions.hasLocationPermission
import com.namoadigital.prj001.extensions.isGpsEnabled
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.service.location.FsTripLocationService
import com.namoadigital.prj001.service.location.util.LocationServiceConstants
import com.namoadigital.prj001.util.ToolBox_Inf

class WorkLocationAvailability(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        try {
            if(!checkLocationPermission()
                || !checkGpsEnable()){
                if (FsTripLocationService.isTracking.value) {
                    context.sendCommandToServiceTripLocation(LocationServiceConstants.STOP_LOCATION)
                }
            }
            return Result.success()
        }catch (exception: Exception){
            ToolBox_Inf.registerException(javaClass.name, exception)
            return Result.success()
        }
    }

    private fun checkGpsEnable(): Boolean {
        return context.isGpsEnabled()
    }

    private fun checkLocationPermission(): Boolean {
        return context.hasLocationPermission()
    }
    companion object{
        const val WORKER_TAG = "WorkGpsPermissionAndEnable"
    }
}