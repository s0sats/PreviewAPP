package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripUserFullUpdateEnv(
    @Expose @SerializedName("userSeq") val userSeq: Int,
    @Expose @SerializedName("userCode") val userCode: Int,
    @Expose @SerializedName("inDate") val dateStart: String,
    @Expose @SerializedName("outDate") val dateEnd: String?,
)
