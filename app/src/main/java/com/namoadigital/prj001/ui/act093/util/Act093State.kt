package com.namoadigital.prj001.ui.act093.util

import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import java.io.Serializable

data class Act093State(
    val isLoading: Boolean = false,
    val list: List<DeviceTpModel> = emptyList(),
    val serialInfo: SerialInfo = SerialInfo()
) {
    data class SerialInfo(
        val originFlow: String? = null,
        val serialId: String? = null,
        val iconColor: String? = null,
        val product: String? = null,
        val model: String? = null,
        val trackings: String? = null,
        val infoAdd: String? = null,
        val hasMeasureTp: Boolean = false,
        val value_suffix: String? = null,
        val last_measure_value: Double? = null,
        val last_measure_date: String? = null,
        val last_cycle_value: Float? = null,
        val last_cycle_date: String? = null,
        val lastUpdateSerial: String? = null,
    ) : Serializable
}
