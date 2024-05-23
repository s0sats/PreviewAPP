package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripFleetSetRec(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("scn") val scn: Int
)
