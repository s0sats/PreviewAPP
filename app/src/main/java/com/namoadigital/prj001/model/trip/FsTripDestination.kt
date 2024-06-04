package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.location.Coordinates

data class FsTripDestination(
    @SerializedName("destinationSeq") val destinationSeq: Int,
    @SerializedName("destinationType") val destinationType: String?,
    @SerializedName("destinationSiteCode") val destinationSiteCode: Int?,
    @SerializedName("destinationSiteDesc") val destinationSiteDesc: String?,
    @SerializedName("destinationRegionCode") val destinationRegionCode: Int?,
    @SerializedName("destinationRegionDesc") val destinationRegionDesc: String?,
    @SerializedName("ticketPrefix") val ticketPrefix: Int?,
    @SerializedName("ticketCode") val ticketCode: Int?,
    @SerializedName("ticketId") val ticketId: String?,
    @SerializedName("destinationStatus") val destinationStatus: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("arrivedDate") val arrivedDate: String?,
    @SerializedName("arrivedLat") val arrivedLat: Double?,
    @SerializedName("arrivedLon") val arrivedLon: Double?,
    @SerializedName("arrivedType") val arrivedType: String?,
    @SerializedName("arrivedFleetOdometer") val arrivedFleetOdometer: Long?,
    @SerializedName("arrivedFleetPhoto") val arrivedFleetPhoto: String?,
    @SerializedName("arrivedFleetPhotoName") val arrivedFleetPhotoName: String?,
    var arrivedFleetPhotoLocal: String? = "",
    @SerializedName("departedDate") val departedDate: String?,
    @SerializedName("departedLat") val departedLat: Double?,
    @SerializedName("departedLon") val departedLon: Double?,
    @SerializedName("departedType") val departedType: String?,
    @SerializedName("countryId") val countryId: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("district") val district: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("streetnumber") val streetnumber: String?,
    @SerializedName("complement") val complement: String?,
    @SerializedName("zipCode") val zipCode: String?,
    @SerializedName("plus_code") val plusCode: String?,
    @SerializedName("contact") val contactName: String?,
    @SerializedName("phone") val contactPhone: String?,
    @SerializedName("siteMainUser") val siteMainUser: Int?,
    @SerializedName("minDate") val minDate: String?,
    @SerializedName("serialCnt") val serialCnt: Int,
    @SerializedName("actions") var actions: MutableList<FsTripDestinationAction>? = mutableListOf(),
) {

    @SerializedName("customerCode")
    var customerCode: Long = -1

    @SerializedName("tripPrefix")
    var tripPrefix: Int = -1

    @SerializedName("tripCode")
    var tripCode: Int = -1


    val isTicket = destinationType == TICKET_DESTINATION_TYPE
    val coordinates = Coordinates(latitude ?: 0.0, longitude ?: 0.0)
    val ticketPk = "$ticketPrefix.$ticketCode"

    constructor(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        destinationType: String?,
        destinationSiteCode: Int?,
        destinationSiteDesc: String?,
        destinationRegionCode: Int?,
        destinationRegionDesc: String?,
        ticketPrefix: Int?,
        ticketCode: Int?,
        ticketId: String?,
        destinationStatus: String?,
        latitude: Double?,
        longitude: Double?,
        arrivedDate: String?,
        arrivedLat: Double?,
        arrivedLon: Double?,
        arrivedType: String?,
        arrivedFleetOdometer: Long?,
        arrivedFleetPhoto: String?,
        arrivedFleetPhotoName: String?,
        arrivedFleetPhotoLocal: String?,
        departedDate: String?,
        departedLat: Double?,
        departedLon: Double?,
        departedType: String?,
        countryId: String?,
        state: String?,
        city: String?,
        district: String?,
        street: String?,
        streetnumber: String?,
        complement: String?,
        zipCode: String?,
        plusCode: String?,
        contactName: String?,
        contactPhone: String?,
        siteMainUser: Int?,
        minDate: String?,
        serialCnt: Int,
    ) : this(
        destinationSeq = destinationSeq,
        destinationType = destinationType,
        destinationSiteCode = destinationSiteCode,
        destinationSiteDesc = destinationSiteDesc,
        destinationRegionCode = destinationRegionCode,
        destinationRegionDesc = destinationRegionDesc,
        ticketPrefix = ticketPrefix,
        ticketCode = ticketCode,
        ticketId = ticketId,
        destinationStatus = destinationStatus,
        latitude = latitude,
        longitude = longitude,
        arrivedDate = arrivedDate,
        arrivedLat = arrivedLat,
        arrivedLon = arrivedLon,
        arrivedType = arrivedType,
        arrivedFleetOdometer = arrivedFleetOdometer,
        arrivedFleetPhoto = arrivedFleetPhoto,
        arrivedFleetPhotoLocal = arrivedFleetPhotoLocal,
        arrivedFleetPhotoName = arrivedFleetPhotoName,
        departedDate = departedDate,
        departedLat = departedLat,
        departedLon = departedLon,
        departedType = departedType,
        countryId = countryId,
        state = state,
        city = city,
        district = district,
        street = street,
        streetnumber = streetnumber,
        complement = complement,
        zipCode = zipCode,
        plusCode = plusCode,
        contactName = contactName,
        contactPhone = contactPhone,
        siteMainUser = siteMainUser,
        minDate = minDate,
        serialCnt = serialCnt
    ) {
        this.customerCode = customerCode
        this.tripPrefix = tripPrefix
        this.tripCode = tripCode
    }

    fun setPk(fsTrip: FSTrip) {
        //
        this.customerCode = fsTrip.customerCode
        this.tripPrefix = fsTrip.tripPrefix
        this.tripCode = fsTrip.tripCode
        //
        actions?.forEach {
            it.setPk(this)
        }
    }

    fun getFullAddress(): String {
        return listOfNotNull(
            street,
            streetnumber?.let { ", $it" },
            complement?.let { ", $it" },
            district?.let { ", $it" },
            city?.let { ", $it" },
            state?.let { " - $it" },
            zipCode?.let { ", $it" }
        ).joinToString("").trimIndent()
    }

    fun getAddress(): String {
        return listOfNotNull(
            street,
            streetnumber?.let { ", $it" },
            city?.let { ", $it" },
        ).joinToString("").trimIndent()
    }

    fun containsAddress(): Boolean {
        return listOf(
            street,
            streetnumber,
            complement,
            district,
            city,
            state,
            zipCode
        ).any { !it.isNullOrEmpty() }
    }

    val arrivedDestinationPhoto = FSTripPhoto(
        pathLocal = arrivedFleetPhotoLocal ?: "",
        pathRemote = arrivedFleetPhotoName ?: "",
        photoUrl = arrivedFleetPhoto ?: ""
    )

    companion object {
        const val TICKET_DESTINATION_TYPE = "TICKET"
        const val SITE_DESTINATION_TYPE = "SITE"
        const val OVER_NIGHT_DESTINATION_TYPE = "OVER_NIGHT"
    }

}