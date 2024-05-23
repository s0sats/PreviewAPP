package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripUserSaveRec(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("userCode") var userCode: Int,
    @SerializedName("userSeq") var userSeq: Int,
)