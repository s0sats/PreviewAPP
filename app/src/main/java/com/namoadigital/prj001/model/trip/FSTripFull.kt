package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripFull (
    @Expose @SerializedName("tripFull") var tripFull: FSTrip?,
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int?,
    @Expose @SerializedName("tripCode") var tripCode: Int?,
    @Expose @SerializedName("scn") var scn: Int?,
)