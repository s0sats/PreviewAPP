package com.namoadigital.prj001.model

import java.io.Serializable


class AcessoryFormView(
    val acessoryName: String,
    val acessoryTracking: String,
    val isReadOnly: Boolean,
    val lastPositionSelected: Int = 0,
    val inspections: List<InspectionCell>
): Serializable {
    var filterVal: String = ""
    var nonForecastFilter: Boolean = false
}