package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripFleetSetEnv(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("destinationSeq") val destinationSeq: Int? = null,
    @SerializedName("scn") val scn: Int,
    @SerializedName("target") val target: String,
    @SerializedName("licensePlate") val licensePlate: String? = null,
    @SerializedName("odometer") val odometer: Long? = null,
    @SerializedName("imageKey") val imageKey: String? = null,
    @SerializedName("imageChanged") val imageChanged: Int,
){
    var deletePhoto: Boolean = false
    companion object {
        val WS_BUNDLE_KEY = TripFleetSetEnv::class.java.name
    }

}