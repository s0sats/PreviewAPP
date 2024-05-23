package com.namoadigital.prj001.ui.act094.destination.domain.destination_availables

data class AvailableDestinationFilter(
    val showOnlyToday: Boolean = false,
    val showOnlyUrgent: Boolean = false,
    val hidePreventive: Boolean = false,
    val showOnlySiteWithPlanning: Boolean = false,
    val siteCode: Int? = null,
) {

    fun isDefault(): Boolean {
        return !showOnlyToday && !showOnlyUrgent && !hidePreventive && !showOnlySiteWithPlanning
    }


}