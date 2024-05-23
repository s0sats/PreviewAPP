package com.namoadigital.prj001.ui.act094.destination.local.preference

import android.content.Context
import android.content.SharedPreferences
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter

class DestinationFilterPreference constructor(
    private val pref: SharedPreferences
) : ModelPreferences<AvailableDestinationFilter> {
    override fun write(model: AvailableDestinationFilter) {
        with(pref) {

            edit()
                .putBoolean(SHOW_ONLY_TODAY, model.showOnlyToday)
                .putBoolean(SHOW_ONLY_PRIORITY, model.showOnlyUrgent)
                .putBoolean(HIDE_PREVENTIVE, model.hidePreventive)
                .putBoolean(SHOW_ONLY_SITE_WITH_PLANNING, model.showOnlySiteWithPlanning)
                .putInt(SITE_CODE, model.siteCode ?: -1)
                .apply()
        }
    }

    override fun read(): AvailableDestinationFilter {
        with(pref) {

            return AvailableDestinationFilter(
                showOnlyToday = getBoolean(SHOW_ONLY_TODAY, false),
                showOnlyUrgent = getBoolean(SHOW_ONLY_PRIORITY, false),
                hidePreventive = getBoolean(HIDE_PREVENTIVE, false),
                showOnlySiteWithPlanning = getBoolean(SHOW_ONLY_SITE_WITH_PLANNING, false),
                siteCode = if (getInt(SITE_CODE, -1) == -1) null else getInt(SITE_CODE, -1)
            )

        }
    }


    companion object {

        const val SHOW_ONLY_TODAY = "SHOW_ONLY_TODAY"
        const val SHOW_ONLY_PRIORITY = "SHOW_ONLY_PRIORITY"
        const val HIDE_PREVENTIVE = "HIDE_PREVENTIVE"
        const val SHOW_ONLY_SITE_WITH_PLANNING = "SHOW_ONLY_SITE_WITH_PLANNING"
        const val SITE_CODE = "SITE_CODE"

        fun instance(context: Context): DestinationFilterPreference {
            return DestinationFilterPreference(
                context.getSharedPreferences("destination_filter", Base_Activity.MODE_PRIVATE)
            )
        }
    }
}