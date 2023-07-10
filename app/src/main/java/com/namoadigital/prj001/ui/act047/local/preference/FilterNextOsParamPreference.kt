package com.namoadigital.prj001.ui.act047.local.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.namoadigital.prj001.core.data.local.preferences.ModelPreferences
import com.namoadigital.prj001.ui.act047.model.NextOsFilter
import com.namoadigital.prj001.ui.act047.model.TypeDeadlineFilter
import com.namoadigital.prj001.ui.act047.model.TypePriorityFilter
import com.namoadigital.prj001.ui.act047.model.TypeStatusFilter

class FilterNextOsParamPreference constructor(
    private val preference: SharedPreferences
) : ModelPreferences<NextOsFilter> {

    override fun write(model: NextOsFilter) {
        with(preference) {
            edit()
                .putString(
                    FILTER_NEXT_OS_STATUS,
                    model.statusListFilterToString().joinToString(SEPARATOR)
                )
                .putString(
                    FILTER_NEXT_OS_DEADLINE,
                    model.deadlineFilterToString().joinToString(SEPARATOR)
                )
                .putString(FILTER_NEXT_OS_PRIORITY, model.priorityFilter.type)
                .apply()
        }

    }

    override fun read(): NextOsFilter {
        with(preference) {
            val statusType = getStatus()
            val deadlineType = getDeadline()
            val priorityType = priorityStringToTypePriorityFilter(
                getString(
                    FILTER_NEXT_OS_PRIORITY,
                    TypePriorityFilter.DEADLINE.type
                ) ?: TypePriorityFilter.DEADLINE.type
            )
            return NextOsFilter(statusType, deadlineType, priorityType)
        }
    }

    private fun SharedPreferences.getStatus(): List<TypeStatusFilter> {
        val values = getString(FILTER_NEXT_OS_STATUS, TypeStatusFilter.PENDING_AND_PROCESS.type)
        return values?.split(SEPARATOR)?.mapNotNull { getTypeStatusFilter(it) } ?: emptyList()
    }

    private fun getTypeStatusFilter(type: String): TypeStatusFilter? {
        return TypeStatusFilter.values().find { it.type == type }
    }

    private fun SharedPreferences.getDeadline(): List<TypeDeadlineFilter> {
        val values = getString(
            FILTER_NEXT_OS_DEADLINE, listOf(
                TypeDeadlineFilter.NOT_EXPIRED.type,
                TypeDeadlineFilter.EXPIRED.type,
                TypeDeadlineFilter.WITHOUT.type
            ).joinToString(SEPARATOR)
        )
        values?.let { filter ->
            return filter.split(SEPARATOR).mapNotNull { getTypeDeadlineFilter(it) }
        }

        return emptyList()
    }

    private fun getTypeDeadlineFilter(type: String): TypeDeadlineFilter? {
        return TypeDeadlineFilter.values().find { it.type == type }
    }

    private fun priorityStringToTypePriorityFilter(type: String) =
        TypePriorityFilter.values().find { it.type == type } ?: TypePriorityFilter.DEADLINE


    companion object {

        private const val SEPARATOR = "|"

        fun instancePref(context: Context): FilterNextOsParamPreference {
            return FilterNextOsParamPreference(
                PreferenceManager.getDefaultSharedPreferences(context)
            )
        }


        const val FILTER_NEXT_OS_STATUS = "FILTER_NEXT_OS_STATUS"
        const val FILTER_NEXT_OS_DEADLINE = "FILTER_NEXT_OS_DEADLINE"
        const val FILTER_NEXT_OS_PRIORITY = "FILTER_NEXT_OS_PRIORITY"
    }
}