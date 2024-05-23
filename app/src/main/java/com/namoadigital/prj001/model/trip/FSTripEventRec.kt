package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FSTripEventRec(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("eventSeq") var eventSeq: Int
)