package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FSTripEvent(
    @SerializedName("eventSeq") var eventSeq: Int,
    @SerializedName("eventTypeCode") var eventTypeCode: Int,
    @SerializedName("eventTypeDesc") var eventTypeDesc: String?,
    @SerializedName("eventCost") var cost: Double?,
    @SerializedName("eventComments") var comment: String?,
    var photoLocal: String?,
    @SerializedName("eventPhotoName") var photoName: String?,
    @SerializedName("eventPhoto") var photoUrl: String?,
    @SerializedName("eventStatus") var eventStatus: String?,
    @SerializedName("eventStart") var eventStart: String?,
    @SerializedName("eventEnd") var eventEnd: String?,
    @SerializedName("eventTime") var eventTime: String?,
    @SerializedName("eventAllowedTime") var allowedTime: String?,
    @SerializedName("eventTimeAlert") var timeAlert: Int?,
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
        timeAlert = eventTimeAlert
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