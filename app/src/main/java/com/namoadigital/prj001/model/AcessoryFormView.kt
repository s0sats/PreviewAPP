package com.namoadigital.prj001.model

import java.io.Serializable


class AcessoryFormView(
    val acessoryName: String,
    val acessoryTracking: String?,
    val isReadOnly: Boolean,
    var lastPositionSelected: Int = 0,
    val devicePkPrefix: String,
    val inspections: MutableList<InspectionCell> = mutableListOf()
): Serializable {
    var filterVal: String = ""
    var nonForecastFilter: Boolean = false
    var tabIndex: Int = 0
}