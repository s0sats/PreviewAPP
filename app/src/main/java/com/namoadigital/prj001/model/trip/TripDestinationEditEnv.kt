package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv.Companion.TYPE_MANUAL

data class TripDestinationEditEnv(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("scn") val scn: Int,
    @SerializedName("destinationSeq") val destinationSeq: Int,
    @SerializedName("arrivedDate") val arrivedDate: String,
    @SerializedName("departedDate") val departedDate: String,
){

    companion object {
        val WS_BUNDLE_KEY = TripDestinationEditEnv::class.java.name
    }

}