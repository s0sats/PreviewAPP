package com.namoadigital.prj001.model

import java.io.Serializable

class Act011FormTab (
    val page: Int,
    val name: String,
    val tracking: String?,
    val fieldCount: Int,
    val problemReportedCount: Int?,
    val forecastCount: Int?,
    val criticalForecastCount: Int?,
    val nonForecastCount: Int?,
    val status: String
): Serializable