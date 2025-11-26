package com.namoadigital.prj001.ui.act005.home.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.ui.act005.home.data.preference.RoutineCleaningPreference
import com.namoadigital.prj001.ui.act005.home.di.model.RoutineCleaning
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.worker.Work_Cleanning_Data
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class RoutineCleaningRepositoryImp(
    private val context: Context,
    private val pref: RoutineCleaningPreference = RoutineCleaningPreference(context)
) : RoutineCleaningRepository {


    private val TIME_DIFF = 60 * 60 * 1000

    override fun routineCleaningTables() {
        val prefDate = pref.read().cleaningDate

        prefDate?.let { date ->
            val dateFormat = SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT)
            val beforeDate = dateFormat.parse(date)
            val currentDate = dateFormat.parse(getCurrentDateApi())

            val diff = (currentDate?.time ?: 0L) - (beforeDate?.time ?: 0L)
            var diffHours = diff / TIME_DIFF

            if (diffHours >= 12) {
                runWork()
            }

        } ?: runWork()
    }

    override fun changeDateInPreference(date: String) {
        pref.write(RoutineCleaning(date))
    }


    private fun runWork() {
        val routineCleanWork = OneTimeWorkRequest.Builder(Work_Cleanning_Data::class.java)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                1,
                TimeUnit.HOURS
            )
            .build()

        WorkManager.getInstance(context).enqueue(routineCleanWork)
    }
}