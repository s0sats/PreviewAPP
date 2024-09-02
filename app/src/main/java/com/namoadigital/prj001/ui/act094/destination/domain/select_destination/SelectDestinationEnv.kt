package com.namoadigital.prj001.ui.act094.destination.domain.select_destination

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env

data class SelectDestinationEnv(
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int,
    @Expose @SerializedName("tripCode") var tripCode: Int,
    @Expose @SerializedName("scn") var scn: Int,
    @Expose @SerializedName("destinationType") var destinationType: String?,
    @Expose @SerializedName("siteCode") var siteCode: Int? = null,
    @Expose @SerializedName("destinationTicketPrefix") val destinationTicketPrefix: Int?=null,
    @Expose @SerializedName("destinationTicketCode") val destinationTicketCode: Int?=null,
    @Expose @SerializedName("currentLat") val currentLat: Double?=null,
    @Expose @SerializedName("currentLon") val currentLon: Double?=null,
)
