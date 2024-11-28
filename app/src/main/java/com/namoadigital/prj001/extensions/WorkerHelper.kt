package com.namoadigital.prj001.extensions

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.namoadigital.prj001.dao.EV_UserDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.model.EV_User
import com.namoadigital.prj001.sql.EV_User_Sql_001
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.worker.WorkDownloadTicket
import java.util.concurrent.TimeUnit

fun scheduleDownloadTicket(context: Context?) {

    context?.let {
        val ticketDao = TK_TicketDao(context)
        val tkTicketCachedao = TkTicketCacheDao(context)
        val evUserdao = EV_UserDao(context)
        val user: EV_User = evUserdao.getByString(
            EV_User_Sql_001(
                ToolBox_Con.getPreference_User_Code(context)
            ).toSqlQuery()
        )
        //
        if(user.admin == 0) {
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
}