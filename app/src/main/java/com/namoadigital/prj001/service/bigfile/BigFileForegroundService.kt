package com.namoadigital.prj001.service.bigfile

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.di.services.qualifiers.ForegroundServiceNotification
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.worker.big_file.utils.BigFileManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BigFileForegroundService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    @ForegroundServiceNotification
    lateinit var notificationBuilder: NotificationCompat.Builder
    //
    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )
    //
    private var structureJob: Job? = null
    private var ticketJob: Job? = null
    //
    private val hmAuxTrans by lazy {
        loadTranslate(applicationContext)
    }
    //
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = notificationBuilder
                .setContentTitle(hmAuxTrans[BIG_FILE_NOTIFICATION_TTL])
                .setContentText(hmAuxTrans[BIG_FILE_NOTIFICATION_MSG])
                .build()
            //
            startForeground(BIG_FILE_NOTIFICATION_ID, notification)
        }
//        Log.d("BIG_FILE_PROCESS", "--------onStartCommand----------")
//        Log.d("BIG_FILE_PROCESS", "action: ${intent?.action}")
        when (intent?.action) {
            ACTION_START_TICKET -> {
                isTracking.value = true
                ticketBigFileFlow()
            }
            ACTION_START_STRUCTURE -> {
                isTracking.value = true
                structureBigFileFlow()
            }
            ACTION_STOP_BIG_FILE -> {
                isTracking.value = false
                stopTicketBigFileFlow()
                stopStructureBigFileFlow()
                stopNow()
            }
        }

        return START_NOT_STICKY
    }

    private fun stopStructureBigFileFlow() {
        structureJob?.cancel()
    }

    private fun stopTicketBigFileFlow() {
        ticketJob?.cancel()
    }

    private fun structureBigFileFlow() {
        if(structureJob?.isActive == true){
            return
        }
        //
        structureJob = serviceScope.launch {
            try {
                val bigFileManager = BigFileManager(applicationContext);
                bigFileManager.executeStructure()
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
            }
        }
        //
        structureJob?.invokeOnCompletion {
            checkIfShouldStopService()
        }
    }

    private fun ticketBigFileFlow() {
        if(ticketJob?.isActive == true){
            return
        }
        //
        ticketJob = serviceScope.launch {
            try {
                val bigFileManager = BigFileManager(applicationContext);
                bigFileManager.executeTicket();
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
            }
        }
        //
        ticketJob?.invokeOnCompletion {
            checkIfShouldStopService()
        }
    }

    private fun checkIfShouldStopService(){
//        Log.d("BIG_FILE_PROCESS", "structureJob: ${structureJob?.isCompleted}")
//        Log.d("BIG_FILE_PROCESS", "ticketJob: ${ticketJob?.isCompleted}")
        //
        if (isStructureJobDone() && isTicketJobDone()) {
            stopNow()
        }
    }

    private fun isTicketJobDone(): Boolean = ticketJob == null || ticketJob?.isCompleted == true || ticketJob?.isCancelled == true

    private fun isStructureJobDone(): Boolean = structureJob == null || structureJob?.isCompleted == true || structureJob?.isCancelled == true


    private fun stopNow() {
        serviceScope.cancel()
        isTracking.value = false
//        Log.d("BIG_FILE_PROCESS", "--------stopNow----------")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }



    override fun onDestroy() {
        serviceScope.cancel()
        isTracking.value = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun loadTranslate(context: Context): HMAux {
        listOf(
            BIG_FILE_NOTIFICATION_TTL,
            BIG_FILE_NOTIFICATION_MSG,
        ).let { list ->
            return TranslateResource(
                context,
                Constant.APP_MODULE,
                context.getResourceCode(RESOURCE_BIG_FILE_SERVICE)
            ).setLanguage(list)
        }
    }

    companion object {
        var isTracking = MutableStateFlow(false)

        const val BIG_FILE_NOTIFICATION_ID = 679
        const val ACTION_START_TICKET = "ACTION_START_TICKET"
        const val ACTION_START_STRUCTURE = "ACTION_START_STRUCTURE"
        const val ACTION_STOP_BIG_FILE = "ACTION_STOP"

        private const val RESOURCE_BIG_FILE_SERVICE = "big_file_service"
        const val BIG_FILE_NOTIFICATION_TTL = "big_file_notification_ttl"
        const val BIG_FILE_NOTIFICATION_MSG = "big_file_notification_msg"
    }
}