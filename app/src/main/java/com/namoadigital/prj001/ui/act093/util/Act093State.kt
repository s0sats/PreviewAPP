package com.namoadigital.prj001.ui.act093.util

data class Act093State(
    val isLoading: Boolean = false,
    val serialInfo: SerialInfo = SerialInfo()
) {
    data class SerialInfo(
        val serialId: String? = null,
        val iconColor: String? = null,
        val product: String? = null,
        val model: String? = null,
        val trackings: String? = null,
        val last_measure_value: String? = null,
        val last_measure_date: String? = null,
        val last_cycle_value: String? = null,
    )
}
