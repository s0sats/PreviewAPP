package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripOriginEnv(
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
    @Expose @SerializedName("scn") val scn: Int,
    @Expose @SerializedName("originType") val originType: String? = null,
    @Expose @SerializedName("originSiteCode") val originSiteCode: Int? = null,
    val siteDesc: String? = null,
    @Expose @SerializedName("lat") val lat: Double? = null,
    @Expose @SerializedName("lon") val lon: Double? = null,
    @Expose @SerializedName("originDate") val originDate: String
){

    companion object{
        val WS_BUNDLE_KEY = FSTripOriginEnv::class.java.name

    }

}