package com.namoadigital.prj001.extensions

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.worker.WorkDownloadTicket
import com.namoadigital.prj001.worker.big_file.WorkCheckBigFile
import java.util.concurrent.TimeUnit

fun scheduleDownloadTicket(context: Context?) {
    context?.let {
        val ticketDao = TK_TicketDao(context)
        val tkTicketCachedao = TkTicketCacheDao(context)
        val syncList = ticketDao.syncList
        syncList.addAll(
            tkTicketCachedao.getSyncTicket(
                ToolBox_Con.getPreference_User_Code(
                    context
                )
            )
        )
        if (syncList.isNotEmpty()) {
            val constraints =
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val workDownloadTickets =
                OneTimeWorkRequest.Builder(WorkDownloadTicket::class.java)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        30,
                        TimeUnit.SECONDS
                    )
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(it)
                .enqueueUniqueWork(
                    WorkDownloadTicket.WORKER_TAG,
                    ExistingWorkPolicy.KEEP,
                    workDownloadTickets
                )
        }
    }
}

fun cancelTicketDownloadWorker(context: Context?){
    context?.let{
//        Log.d("SYNC_STRUCTURE", "------------------cancelUniqueWork--------------------")
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(WorkDownloadTicket.WORKER_TAG)
        workManager.cancelUniqueWork(WorkCheckBigFile.Companion.WORKER_TAG)
    }

}