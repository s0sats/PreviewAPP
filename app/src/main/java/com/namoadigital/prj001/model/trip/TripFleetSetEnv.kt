package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TripFleetSetEnv(
    @Expose
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose
    @SerializedName("tripCode") val tripCode: Int,
    @Expose
    @SerializedName("destinationSeq") val destinationSeq: Int? = null,
    @Expose
    @SerializedName("scn") val scn: Int,
    @Expose
    @SerializedName("target") val target: String,
    @Expose
    @SerializedName("licensePlate") val licensePlate: String? = null,
    @Expose
    @SerializedName("odometer") val odometer: Long? = null,
    @Expose
    @SerializedName("imageKey") val imageKey: String? = null,
    @Expose
    @SerializedName("imageChanged") val imageChanged: Int,
){
    var deletePhoto: Boolean = false
    var photoChanged = if(deletePhoto) 1 else imageChanged
    companion object {
        val WS_BUNDLE_KEY = TripFleetSetEnv::class.java.name
    }

}