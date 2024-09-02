package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.location.Coordinates

data class FSTrip(
    @Expose @SerializedName("customerCode") var customerCode: Long,
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int,
    @Expose @SerializedName("tripCode") var tripCode: Int,
    @Expose @SerializedName("tripUserCode") var tripUserCode: Int,
    @Expose @SerializedName("tripUserName") var tripUserName: String,
    @Expose @SerializedName("tripStatus") var tripStatus: String,
    @Expose @SerializedName("fleetLicencePlate") var fleetLicencePlate: String? = null,
    @Expose @SerializedName("fleetStartOdometer") var fleetStartOdometer: Long? = null,
    @Expose @SerializedName("fleetStartPhoto") var fleetStartPhoto: String? = null,
    @Expose @SerializedName("fleetStartPhotoName") var fleetStartPhotoName: String? = null,
    @Expose @SerializedName("fleetStartPhotoChanged") var fleetStartPhotoChanged: Int = 0,
    var fleetStartPhotoLocal: String? = null,
    @Expose @SerializedName("fleetEndOdometer") var fleetEndOdometer: Long? = null,
    @Expose @SerializedName("fleetEndPhoto") var fleetEndPhoto: String? = null,
    @Expose @SerializedName("fleetEndPhotoName") var fleetEndPhotoName: String? = null,
    @Expose @SerializedName("fleetEndPhotoChanged") var fleetEndPhotoChanged: Int = 0,
    var fleetEndPhotoLocal: String? = null,
    @Expose @SerializedName("originType") var originType: String? = null,
    @Expose @SerializedName("originSiteCode") var originSiteCode: Int? = null,
    @Expose @SerializedName("originSiteDesc") var originSiteDesc: String? = null,
    @Expose @SerializedName("originLat") var originLat: Double? = null,
    @Expose @SerializedName("originLon") var originLon: Double? = null,
    @Expose @SerializedName("originDate") var originDate: String? = null,
    @Expose @SerializedName("positionLat") var positionLat: Double? = null,
    @Expose @SerializedName("positionLon") var positionLon: Double? = null,
    @Expose @SerializedName("positionDistanceMin") var positionDistanceMin: Double? = null,
    @Expose @SerializedName("requireDestinationFleetData") var requireDestinationFleetData: Int,
    @Expose @SerializedName("positionDate") var positionDate: String? = null,
    var syncRequired: Int = 0,
    @Expose @SerializedName("scn") var scn: Int,
    @Expose @SerializedName("requireFleetData") var requireFleetData: Int,
    @Expose @SerializedName("distanceRefMinutes") var distanceRefMinutes: Int = 5,
    @Expose @SerializedName("distanceRefMinutesTrans") var distanceRefMinutesTrans: Int = 10,
    @Expose @SerializedName("doneDate") var doneDate: String? = null,
    @Expose @SerializedName("users") var users: MutableList<FSTripUser>? = mutableListOf(),
    @Expose @SerializedName("events") var events: MutableList<FSTripEvent>? = mutableListOf(),
    @Expose @SerializedName("destinations") var destinations: MutableList<FsTripDestination>? = mutableListOf(),
    var updateRequired: Int = 0,
) {
    val hasUpdateRequired = updateRequired == 1
    val hasSyncRequired = syncRequired == 1
    val isRequiredFleetData = requireFleetData == 1
    val isRequireDestinationFleetData = requireDestinationFleetData == 1

    fun setPk() {
        //
        users?.forEach {
            it.setPk(this)
        }
        //
        events?.forEach {
            it.setPk(this)
        }
        //
        destinations?.forEach {
            it.setPk(this)
        }
        //
    }


    val originCoordinates = Coordinates(originLat ?: 0.0, originLon ?: 0.0, originDate)
    val positionCoordinates = Coordinates(positionLat ?: 0.0, positionLon ?: 0.0, positionDate)

    val prefixAndCode = Pair(tripPrefix, tripCode)

    val photoFleetStart = FSTripPhoto(
        pathLocal = fleetStartPhotoLocal ?: "",
        pathRemote = fleetStartPhotoName ?: "",
        photoUrl = fleetStartPhoto ?: ""
    )

    val photoEnd = FSTripPhoto(
        pathLocal = fleetEndPhotoLocal ?: "",
        pathRemote = fleetEndPhotoName ?: "",
        photoUrl = fleetEndPhoto ?: ""
    )


    companion object {
        const val TRIP_FULL_ERROR = "TRIP_FULL_ERROR"

    }

}