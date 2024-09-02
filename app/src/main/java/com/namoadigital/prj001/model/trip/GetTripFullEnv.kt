package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetTripFullEnv(
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
){
    companion object {

        val WS_BUNDLE_KEY = GetTripFullEnv::class.java.name

    }
}