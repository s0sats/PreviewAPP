package com.namoadigital.prj001.ui.act094.destination.domain.select_destination

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env

data class SelectDestinationEnv(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("destinationType") var destinationType: String?,
    @SerializedName("siteCode") var siteCode: Int? = null,
    @SerializedName("destinationTicketPrefix") val destinationTicketPrefix: Int?=null,
    @SerializedName("destinationTicketCode") val destinationTicketCode: Int?=null,
    @SerializedName("currentLat") val currentLat: Double?=null,
    @SerializedName("currentLon") val currentLon: Double?=null,
) : Main_Header_Env() {

}
