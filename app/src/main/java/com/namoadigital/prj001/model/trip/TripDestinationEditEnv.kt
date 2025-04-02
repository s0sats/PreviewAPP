package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TripDestinationEditEnv(
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
    @Expose @SerializedName("scn") val scn: Int,
    @Expose @SerializedName("destinationSeq") val destinationSeq: Int,
    @Expose @SerializedName("arrivedDate") val arrivedDate: String,
    @Expose @SerializedName("departedDate") val departedDate: String,
){

    companion object {
        val WS_BUNDLE_KEY = TripDestinationEditEnv::class.java.name
    }

}