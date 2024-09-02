package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.service.trip.WsAvailablesDestinations

data class TripDestinationStatusChangeEnv(
   @Expose @SerializedName("tripPrefix") var tripPrefix: Int ,
   @Expose @SerializedName("tripCode") var tripCode: Int,
   @Expose @SerializedName("destinationSeq") var destinationSeq: Int,
   @Expose @SerializedName("scn") var scn: Int,
   @Expose @SerializedName("destinationStatus") var destinationStatus: String,
   @Expose @SerializedName("changeType") var changeType: String = TYPE_MANUAL,
){
    companion object {

        val WS_BUNDLE_KEY = TripDestinationStatusChangeEnv::class.java.name
        val TYPE_MANUAL = "MANUAL"
        val TYPE_AUTO = "AUTO"
    }
}