package com.namoadigital.prj001.core.data.local.preferences.environment

import android.content.Context
import android.content.SharedPreferences
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.data.domain.model.EnvironmentPrefModel
import com.namoadigital.prj001.core.data.domain.model.EnvironmentType
import com.namoadigital.prj001.core.data.domain.model.toEnvironmentType
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences

class EnvironmentDevelopementPref constructor(
    private val pref: SharedPreferences
) : ModelPreferences<EnvironmentPrefModel> {
    override fun write(model: EnvironmentPrefModel) {
        with(pref) {
            edit()
                .putString(
                    ENVIRONMENT_TYPE,
                    model.environment.string
                )
                .apply()
        }
    }

    override fun read(): EnvironmentPrefModel {
        with(pref) {
            return EnvironmentPrefModel(
                getString(
                    ENVIRONMENT_TYPE,
                    EnvironmentType.NULL.string
                ).toEnvironmentType()
            )
        }
    }


    companion object {
        const val ENVIRONMENT_TYPE = "ENVIRONMENT_TYPE"


        fun instance(context: Context): EnvironmentDevelopementPref {
            return EnvironmentDevelopementPref(
                context.getSharedPreferences("environment_development", Base_Activity.MODE_PRIVATE)
            )
        }
    }
}