package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripNewRec(
    @SerializedName("tripPrefix") val tripPrefix:Int,
    @SerializedName("tripCode") val tripCode:Int,
    @SerializedName("scn") val scn:Int,
    @SerializedName("originDate") val originDate: String,
    @SerializedName("tripStatus") val tripStatus: String,
    @SerializedName("originType") val originType: String,
    @SerializedName("originSiteCode") val originSiteCode: Int?,
    @SerializedName("originSiteDesc") val originSiteDesc: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("positionDistanceMin") val positionDistanceMin: Double,
    @SerializedName("requireFleetData") val requireFleetData: Int?,
    @SerializedName("requireDestinationFleetData") val requireDestinationFleetData: Int?, 
    @SerializedName("distanceRefMinutes") val distanceRefMinutes: Int = 5,
    @SerializedName("distanceRefMinutesTrans") val distanceRefMinutesTrans: Int = 10,
)