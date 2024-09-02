package com.namoadigital.prj001.extensions

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.namoadigital.prj001.dao.EV_User_CustomerDao
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.worker.WorkDownloadTicket
import java.util.concurrent.TimeUnit

fun scheduleDownloadTicket(context: Context?) {
    val evUserCustomerdao =
        EV_User_CustomerDao(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE)
    val userInfo = evUserCustomerdao.loggedUserCustomer

    context?.let {
        if (userInfo.field_service == 1
            || context.isCurrentTrip()
        ) {
            val constraints =
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val workDownloadTickets =
                OneTimeWorkRequest.Builder(WorkDownloadTicket::class.java)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        10,
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