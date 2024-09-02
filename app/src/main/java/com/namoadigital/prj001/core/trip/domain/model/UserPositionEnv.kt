package com.namoadigital.prj001.core.trip.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPositionEnv(
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
    @Expose @SerializedName("lat") val lat: Double,
    @Expose @SerializedName("lon") val lon: Double,
    @Expose @SerializedName("gpsDate") val gpsDate: String,
    @Expose @SerializedName("destinationSeq") val destinationSeq: Int?,
    @Expose @SerializedName("alert") val alert: String,
    @Expose @SerializedName("speed") val speed: Double? = null,
    @Expose @SerializedName("isRef") val isRef: Int? = 0
){

    companion object {
        val NAME = UserPositionEnv::class.java.name
    }

}