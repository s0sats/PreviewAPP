package com.namoadigital.prj001.ui.act093.util

import com.namoadigital.prj001.ui.act093.model.DeviceTpModel

data class Act093State(
    val isLoading: Boolean = false,
    val list: List<DeviceTpModel> = emptyList(),
    val serialInfo: SerialInfo = SerialInfo()
) {
    data class SerialInfo(
        val serialId: String? = null,
        val iconColor: String? = null,
        val product: String? = null,
        val model: String? = null,
        val trackings: String? = null,
        val value_suffix: String? = null,
        val last_measure_value: Int? = null,
        val last_measure_date: String? = null,
        val last_cycle_value: Int? = null,
    )
}
