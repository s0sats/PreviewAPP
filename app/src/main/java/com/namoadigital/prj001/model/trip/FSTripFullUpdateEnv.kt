package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripFullUpdateEnv(
    @Expose @SerializedName("customerCode") var customerCode: Long,
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int,
    @Expose @SerializedName("tripCode") var tripCode: Int,
    @Expose @SerializedName("scn") var scn: Int,
    @Expose @SerializedName("tripStatus") var tripStatus: String,
    @Expose @SerializedName("fleetLicencePlate") var fleetLicencePlate: String? = null,
    @Expose @SerializedName("fleetStartOdometer") var fleetStartOdometer: Long? = null,
    @Expose @SerializedName("fleetEndOdometer") var fleetEndOdometer: Long? = null,
    @Expose @SerializedName("originType") var originType: String? = null,
    @Expose @SerializedName("originSiteCode") var originSiteCode: Int? = null,
    @Expose @SerializedName("originLat") var originLat: Double? = null,
    @Expose @SerializedName("originLon") var originLon: Double? = null,
    @Expose @SerializedName("originDate") var originDate: String? = null,
    @Expose @SerializedName("doneDate") var doneDate: String? = null,
    @Expose @SerializedName("fleetStartPhotoChanged") var fleetStartPhotoChanged: Int = 0,
    @Expose @SerializedName("fleetStartPhotoKey") var fleetStartPhotoKey: String? = null,
    @Expose @SerializedName("fleetEndPhotoChanged") var fleetEndPhotoChanged: Int = 0,
    @Expose @SerializedName("fleetEndPhotoKey") var fleetEndPhotoKey: String? = null,
    @Expose @SerializedName("users") var users: List<FSTripUserFullUpdateEnv>? = emptyList(),
    @Expose @SerializedName("events") var events: List<FSTripEventFullUpdateEnv>? = emptyList(),
    @Expose @SerializedName("destinations") var destinations: List<FsTripDestinationFullUpdateEnv>? = emptyList(),
)