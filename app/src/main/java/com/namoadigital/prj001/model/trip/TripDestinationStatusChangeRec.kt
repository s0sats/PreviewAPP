package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripDestinationStatusChangeRec (
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("tripStatus") var tripStatus: String,
    @SerializedName("scn") var scn: Int,
    @SerializedName("destinationSeq") var destinationSeq: Int?,
    @SerializedName("destinationStatus") var destinationStatus: String?,
    @SerializedName("nextDestinationSeq") var nextDestinationSeq: Int?,
    @SerializedName("nextDestinationStatus") var nextDestinationStatus: String?,
    @SerializedName("date") var date: String?,
)