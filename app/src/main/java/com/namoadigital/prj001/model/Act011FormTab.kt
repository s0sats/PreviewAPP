package com.namoadigital.prj001.model

import java.io.Serializable

class Act011FormTab (
    val page: Int,
    val name: String,
    val tracking: String?,
    val fieldCount: Int,
    val problemReportedCount: Act011FormCounter?,
    val forecastCount: Act011FormCounter?,
    val criticalForecastCount: Act011FormCounter?,
    val nonForecastCount: Act011FormCounter?,
    val status: Act011FormTabStatus,
    val countInteract: Int? = null,
): Serializable {
    data class Act011FormCounter(
        val done: Int,
        val total: Int
    ) : Serializable {

        fun formattedCounter() = "$done / $total"

    }
}


enum class Act011FormTabStatus{
    OK , PENDING, ERROR
}