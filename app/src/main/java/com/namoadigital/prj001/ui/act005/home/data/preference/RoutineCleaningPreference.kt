package com.namoadigital.prj001.ui.act005.home.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences
import com.namoadigital.prj001.ui.act005.home.di.model.RoutineCleaning

class RoutineCleaningPreference constructor(
    private val pref: SharedPreferences
) : ModelPreferences<RoutineCleaning> {
    override fun write(model: RoutineCleaning) {
        with(pref) {
            edit()
                .putString(RoutineCleaning.CLEANING_DATE, model.cleaningDate)
            .apply()
        }
    }

    override fun read(): RoutineCleaning {
        with(pref){
            return RoutineCleaning(
                cleaningDate = getString(RoutineCleaning.CLEANING_DATE, null)
            )
        }
    }

    companion object INSTANCE {

        operator fun invoke(context: Context) = RoutineCleaningPreference(PreferenceManager.getDefaultSharedPreferences(context))

    }
}