package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FSTripOriginEnv(
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("scn") val scn: Int,
    @SerializedName("originType") val originType: String? = null,
    @SerializedName("originSiteCode") val originSiteCode: Int? = null,
    val siteDesc: String? = null,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("originDate") val originDate: String
){

    companion object{
        val WS_BUNDLE_KEY = FSTripOriginEnv::class.java.name

    }

}