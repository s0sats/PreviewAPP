package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.service.trip.WsAvailablesDestinations

data class TripDestinationStatusChangeEnv(
    @SerializedName("tripPrefix") var tripPrefix: Int ,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("destinationSeq") var destinationSeq: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("destinationStatus") var destinationStatus: String,
    @SerializedName("changeType") var changeType: String = TYPE_MANUAL,
){
    companion object {

        val WS_BUNDLE_KEY = TripDestinationStatusChangeEnv::class.java.name
        val TYPE_MANUAL = "MANUAL"
        val TYPE_AUTO = "AUTO"
    }
}