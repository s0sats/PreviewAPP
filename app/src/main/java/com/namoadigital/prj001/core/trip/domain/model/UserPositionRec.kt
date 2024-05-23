package com.namoadigital.prj001.core.trip.domain.model

import com.google.gson.annotations.SerializedName

data class UserPositionRec(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
)