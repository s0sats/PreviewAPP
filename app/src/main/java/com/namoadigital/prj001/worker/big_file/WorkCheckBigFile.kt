package com.namoadigital.prj001.worker.big_file

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.service.bigfile.BigFileForegroundService
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_SERIAL_STRUCTURE
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_TICKET
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus

class WorkCheckBigFile(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        debugBigFile("WorkCheckBigFile")
        debugBigFile("BigFileForegroundService.isTracking: ${BigFileForegroundService.isTracking.value}")
        debugBigFile("isStopped: $isStopped")
        //
        if (!BigFileForegroundService.isTracking.value
            && !isStopped) {

            try {
                ToolBox_Con.callBigFileService(
                    context,
                    checkBigFileStatus(FILE_TYPE_SERIAL_STRUCTURE)
                )
                //
                ToolBox_Con.callBigFileService(
                    context,
                    checkBigFileStatus(FILE_TYPE_TICKET)

                )
            } catch (exception: Exception){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    exception is android.app.ForegroundServiceStartNotAllowedException) {
                    return Result.retry(); // WorkManager tentará novamente
                }
                ToolBox_Inf.registerException(javaClass.name, exception)
                return Result.failure();
            }
        }
        //
        return Result.success()
    }

    private fun checkBigFileStatus(fileType:String): BigFile {
        val bigFilePreferenceManager = BigFilePreferenceManager(applicationContext, fileType)
        val bigFile = bigFilePreferenceManager.getBigFile()
        if (bigFile.fileStatus == BigFileStatus.NOT_FOUND.name) {
            bigFile.fileStatus = BigFileStatus.PENDING.name
        }
        bigFilePreferenceManager.saveBigFile(bigFile)
        return bigFile
    }

    companion object{
        val WORKER_TAG = "WorkCheckBigFile"
    }


    private fun debugWithNotification(msg: String) {
//        ToolBox.registerException("BIG_FILE", Exception(msg))
    }
}