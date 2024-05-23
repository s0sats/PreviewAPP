package com.namoadigital.prj001.core.trip.domain.model

import com.google.gson.annotations.SerializedName

data class UserPositionEnv(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("gpsDate") val gpsDate: String,
    @SerializedName("destinationSeq") val destinationSeq: Int?,
    @SerializedName("alert") val alert: String,
    @SerializedName("speed") val speed: Double? = null,
    @SerializedName("isRef") val isRef: Int? = 0
){

    companion object {
        val NAME = UserPositionEnv::class.java.name
    }

}