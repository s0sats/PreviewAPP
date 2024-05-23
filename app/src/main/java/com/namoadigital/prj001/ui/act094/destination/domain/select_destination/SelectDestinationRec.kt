package com.namoadigital.prj001.ui.act094.destination.domain.select_destination

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class SelectDestinationRec(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("destinationSeq") var destinationSeq: Int,
    @SerializedName("destinationStatus") var destinationStatus: String,
    @SerializedName("tripStatus") var tripStatus: String,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("arrivedDate") var arrivedDate: String?,
    @SerializedName("arrivedLat") var arrivedLat: Double?,
    @SerializedName("arrivedLon") var arrivedLon: Double?,
    @SerializedName("distanceMin") var distanceMin: Double
) : Main_Header_Rec() {

}
