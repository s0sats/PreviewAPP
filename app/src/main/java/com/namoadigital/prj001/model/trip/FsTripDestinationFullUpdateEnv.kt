package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FsTripDestinationFullUpdateEnv(
    @Expose @SerializedName("destinationSeq") val destinationSeq: Int,
    @Expose @SerializedName("destinationType") val destinationType: String?,
    @Expose @SerializedName("destinationSiteCode") val destinationSiteCode: Int?,
    @Expose @SerializedName("ticketPrefix") val ticketPrefix: Int?,
    @Expose @SerializedName("ticketCode") val ticketCode: Int?,
    @Expose @SerializedName("destinationStatus") val destinationStatus: String?,
    @Expose @SerializedName("latitude") val latitude: Double?,
    @Expose @SerializedName("longitude") val longitude: Double?,
    @Expose @SerializedName("arrivedDate") val arrivedDate: String?,
    @Expose @SerializedName("arrivedLat") val arrivedLat: Double?,
    @Expose @SerializedName("arrivedLon") val arrivedLon: Double?,
    @Expose @SerializedName("arrivedType") val arrivedType: String?,
    @Expose @SerializedName("arrivedFleetOdometer") val arrivedFleetOdometer: Long?,
    @Expose @SerializedName("departedDate") val departedDate: String?,
    @Expose @SerializedName("departedLat") val departedLat: Double?,
    @Expose @SerializedName("departedLon") val departedLon: Double?,
    @Expose @SerializedName("departedType") val departedType: String?,
    @Expose @SerializedName("arrivedFleetPhotoChanged") val arrivedFleetPhotoChanged: Int = 0,
    @Expose @SerializedName("arrivedFleetPhotoKey") val arrivedFleetPhotoKey: String?,
)
