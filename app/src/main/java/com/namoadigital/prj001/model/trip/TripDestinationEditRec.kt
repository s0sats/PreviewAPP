package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripDestinationEditRec(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int
)