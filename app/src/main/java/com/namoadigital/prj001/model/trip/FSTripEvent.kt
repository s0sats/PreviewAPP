package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripEvent(
    @Expose @SerializedName("eventSeq") var eventSeq: Int,
    @Expose @SerializedName("eventTypeCode") var eventTypeCode: Int,
    @Expose @SerializedName("eventTypeDesc") var eventTypeDesc: String?,
    @Expose @SerializedName("eventCost") var cost: Double?,
    @Expose @SerializedName("eventComments") var comment: String?,
    var photoLocal: String?,
    @Expose @SerializedName("eventPhotoName") var photoName: String?,
    @Expose @SerializedName("eventPhoto") var photoUrl: String?,
    @Expose @SerializedName("eventStatus") var eventStatus: String?,
    @Expose @SerializedName("eventStart") var eventStart: String?,
    @Expose @SerializedName("eventEnd") var eventEnd: String?,
    @Expose @SerializedName("eventTime") var eventTime: String?,
    @Expose @SerializedName("eventPhotoChanged") var eventPhotoChanged: Int,
    @Expose @SerializedName("eventAllowedTime") var allowedTime: String?,
    @Expose @SerializedName("eventTimeAlert") var timeAlert: Int?,
    @Expose @SerializedName("destinationSeq") var destinationSeq: Int?,
) {
    @SerializedName("customerCode")
    var customerCode: Long = -1

    @SerializedName("tripPrefix")
    var tripPrefix: Int = -1

    @SerializedName("tripCode")
    var tripCode: Int = -1

    var waitAllowed: Boolean = false

    constructor(
        customerCode: Long,
        tripPrefix: Int,
        eventTypeCode: Int,
        tripCode: Int,
        eventSeq: Int,
        eventTypeDesc: String?,
        eventStatus: String?,
        eventStart: String?,
        eventEnd: String?,
        eventTime: String?,
        eventAllowedTime: String?,
        eventTimeAlert: Int?,
        cost: Double?,
        comment: String?,
        photoLocal: String?,
        photoName: String?,
        photoUrl: String?,
        eventPhotoChanged: Int,
        destinationSeq: Int?,
    ) : this(
        eventSeq = eventSeq,
        eventTypeCode = eventTypeCode,
        eventTypeDesc = eventTypeDesc,
        cost = cost,
        comment = comment,
        photoLocal = photoLocal,
        photoName = photoName,
        photoUrl = photoUrl,
        eventStart = eventStart,
        eventEnd = eventEnd,
        eventStatus = eventStatus,
        eventTime = eventTime,
        allowedTime = eventAllowedTime,
        timeAlert = eventTimeAlert,
        eventPhotoChanged = eventPhotoChanged,
        destinationSeq = destinationSeq
    ) {
        this.customerCode = customerCode
        this.tripPrefix = tripPrefix
        this.tripCode = tripCode
    }

    val isFinalized: Boolean = !eventEnd.isNullOrEmpty()

    fun setPk(fsTrip: FSTrip) {
        this.customerCode = fsTrip.customerCode
        this.tripPrefix = fsTrip.tripPrefix
        this.tripCode = fsTrip.tripCode
    }

    fun getEventPhoto(): FSTripPhoto{
        return FSTripPhoto(
            pathLocal = photoLocal ?: "",
            pathRemote = photoName ?: "",
            photoUrl = photoUrl ?: ""
        )

    }

}