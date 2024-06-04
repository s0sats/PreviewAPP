package com.namoadigital.prj001.ui.act005.home.data.repository

import android.content.Context
import com.namoadigital.prj001.ui.base.NamoaFactory

interface RoutineCleaningRepository {

    fun routineCleaningTables()

    fun changeDateInPreference(date: String)


    companion object {
        class RoutineCleaningRepositoryFactory(private val context: Context) : NamoaFactory<RoutineCleaningRepository>() {
            override fun build(): RoutineCleaningRepository {
                return RoutineCleaningRepositoryImp(context)
            }
        }

    }
}