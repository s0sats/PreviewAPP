package com.namoadigital.prj001.model.masterdata.ge_os

import com.namoadigital.prj001.R

enum class GeOsDeviceItemStatusColor(val status: String) {
    GRAY("GRAY"),
    RED("RED"),
    YELLOW("YELLOW"),
    BLUE("BLUE");

    companion object {
        fun toColor(colorItem: GeOsDeviceItemStatusColor?): Int? {
            return when (colorItem) {
                RED -> R.color.namoa_os_form_problem_red
                YELLOW -> R.color.namoa_os_form_critical_forecast_yellow
                GRAY -> R.color.namoa_color_gray_7
                BLUE -> R.color.namoa_color_pipeline_origin_icon
                else -> null
            }
        }

        val colorPriority = mapOf(
            RED to 0,
            YELLOW to 1,
            BLUE to 2,
            GRAY to 3
        )

        fun getStatusColor(status: String?): GeOsDeviceItemStatusColor? {
            status?.let{
                return GeOsDeviceItemStatusColor.entries.find { it.status == status }
            }
            return null
        }
    }
}