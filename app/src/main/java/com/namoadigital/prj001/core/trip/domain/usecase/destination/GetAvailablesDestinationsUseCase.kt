package com.namoadigital.prj001.core.trip.domain.usecase.destination

import android.os.Bundle
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository

class GetAvailablesDestinationsUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<Unit, Unit> {

    private fun booleanToInt(value: Boolean) = when (value) {
        true -> 1
        false -> 0
    }

    override fun invoke(input: Unit) {
        val model = repository.getDestinationFilterPreference()
        Bundle().apply {
            putInt(SHOW_ONLY_TODAY, booleanToInt(model.showOnlyToday))
            putInt(SHOW_ONLY_PRIORITY, booleanToInt(model.showOnlyUrgent))
            putInt(HIDE_PREVENTIVE, booleanToInt(model.hidePreventive))
            putInt(SHOW_ONLY_SITE_WITH_PLANNING, booleanToInt(model.showOnlySiteWithPlanning))
            putInt(SITE_CODE, model.siteCode ?: -1)
        }.let {
            repository.execServiceAvailableDestination(it)
        }
    }

    companion object {

        const val SHOW_ONLY_TODAY = "show_only_today"
        const val SHOW_ONLY_PRIORITY = "show_only_priority"
        const val HIDE_PREVENTIVE = "hide_preventive"
        const val SHOW_ONLY_SITE_WITH_PLANNING = "show_only_site_with_planning"
        const val SITE_CODE = "site_code"

    }

}