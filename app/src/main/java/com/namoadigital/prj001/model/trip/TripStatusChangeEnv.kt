package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripStatusChangeEnv(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("scn") val scn: Int,
    @SerializedName("tripStatus") val tripStatus: String,
    @SerializedName("doneDate") val doneDate: String? =null,
){
    companion object {

        val WS_BUNDLE_KEY = TripStatusChangeEnv::class.java.name

    }
}