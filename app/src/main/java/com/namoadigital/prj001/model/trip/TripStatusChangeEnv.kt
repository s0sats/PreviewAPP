package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TripStatusChangeEnv(
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
    @Expose @SerializedName("scn") val scn: Int,
    @Expose @SerializedName("tripStatus") val tripStatus: String,
    @Expose @SerializedName("doneDate") val doneDate: String? =null,
){
    companion object {

        val WS_BUNDLE_KEY = TripStatusChangeEnv::class.java.name

    }
}