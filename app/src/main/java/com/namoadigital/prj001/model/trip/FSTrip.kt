package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.location.Coordinates

data class FSTrip(
    @SerializedName("customerCode") var customerCode: Long,
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("tripUserCode") var tripUserCode: Int,
    @SerializedName("tripUserName") var tripUserName: String,
    @SerializedName("tripStatus") var tripStatus: String,
    @SerializedName("fleetLicencePlate") var fleetLicencePlate: String? = null,
    @SerializedName("fleetStartOdometer") var fleetStartOdometer: Long? = null,
    @SerializedName("fleetStartPhoto") var fleetStartPhoto: String? = null,
    @SerializedName("fleetStartPhotoName") var fleetStartPhotoName: String? = null,
    var fleetStartPhotoLocal: String? = null,
    @SerializedName("fleetEndOdometer") var fleetEndOdometer: Long? = null,
    @SerializedName("fleetEndPhoto") var fleetEndPhoto: String? = null,
    @SerializedName("fleetEndPhotoName") var fleetEndPhotoName: String? = null,
    var fleetEndPhotoLocal: String? = null,
    @SerializedName("originType") var originType: String? = null,
    @SerializedName("originSiteCode") var originSiteCode: Int? = null,
    @SerializedName("originSiteDesc") var originSiteDesc: String? = null,
    @SerializedName("originLat") var originLat: Double? = null,
    @SerializedName("originLon") var originLon: Double? = null,
    @SerializedName("originDate") var originDate: String? = null,
    @SerializedName("positionLat") var positionLat: Double? = null,
    @SerializedName("positionLon") var positionLon: Double? = null,
    @SerializedName("positionDistanceMin") var positionDistanceMin: Double? = null,
    @SerializedName("requireDestinationFleetData") var requireDestinationFleetData: Int,
    @SerializedName("positionDate") var positionDate: String? = null,
    var syncRequired: Int = 0,
    @SerializedName("scn") var scn: Int,
    @SerializedName("requireFleetData") var requireFleetData: Int,
    @SerializedName("distanceRefMinutes") var distanceRefMinutes: Int=5,
    @SerializedName("distanceRefMinutesTrans") var distanceRefMinutesTrans: Int=10,
    @SerializedName("doneDate") var doneDate: String? = null,
    @SerializedName("users") var users: MutableList<FSTripUser>? = mutableListOf(),
    @SerializedName("events") var events: MutableList<FSTripEvent>? = mutableListOf(),
    @SerializedName("destinations") var destinations: MutableList<FsTripDestination>? = mutableListOf(),

) {

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