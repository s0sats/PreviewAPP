package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TripNewEnv(
    @Expose @SerializedName("lat") val lat: Double?,
    @Expose @SerializedName("lon") val lon: Double?,
){
    companion object {

        val WS_BUNDLE_KEY = TripNewEnv::class.java.name

    }
}