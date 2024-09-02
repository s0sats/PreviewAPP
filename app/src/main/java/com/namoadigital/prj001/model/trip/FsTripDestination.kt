package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.location.Coordinates

data class FsTripDestination(
    @Expose @SerializedName("destinationSeq") val destinationSeq: Int,
    @Expose @SerializedName("destinationType") val destinationType: String?,
    @Expose @SerializedName("destinationSiteCode") val destinationSiteCode: Int?,
    @Expose @SerializedName("destinationSiteDesc") val destinationSiteDesc: String?,
    @Expose @SerializedName("destinationRegionCode") val destinationRegionCode: Int?,
    @Expose @SerializedName("destinationRegionDesc") val destinationRegionDesc: String?,
    @Expose @SerializedName("ticketPrefix") val ticketPrefix: Int?,
    @Expose @SerializedName("ticketCode") val ticketCode: Int?,
    @Expose @SerializedName("ticketId") val ticketId: String?,
    @Expose @SerializedName("destinationStatus") val destinationStatus: String?,
    @Expose @SerializedName("latitude") val latitude: Double?,
    @Expose @SerializedName("longitude") val longitude: Double?,
    @Expose @SerializedName("arrivedDate") val arrivedDate: String?,
    @Expose @SerializedName("arrivedLat") val arrivedLat: Double?,
    @Expose @SerializedName("arrivedLon") val arrivedLon: Double?,
    @Expose @SerializedName("arrivedType") var arrivedType: String?,
    @Expose @SerializedName("arrivedFleetOdometer") val arrivedFleetOdometer: Long?,
    @Expose @SerializedName("arrivedFleetPhoto") val arrivedFleetPhoto: String?,
    @Expose @SerializedName("arrivedFleetPhotoName") val arrivedFleetPhotoName: String?,
    @Expose @SerializedName("arrivedFleetPhotoChanged") val arrivedFleetPhotoChanged: Int = 0,
    var arrivedFleetPhotoLocal: String? = "",
    @Expose @SerializedName("departedDate") val departedDate: String?,
    @Expose @SerializedName("departedLat") val departedLat: Double?,
    @Expose @SerializedName("departedLon") val departedLon: Double?,
    @Expose @SerializedName("departedType") var departedType: String?,
    @Expose @SerializedName("countryId") val countryId: String?,
    @Expose @SerializedName("state") val state: String?,
    @Expose @SerializedName("city") val city: String?,
    @Expose @SerializedName("district") val district: String?,
    @Expose @SerializedName("street") val street: String?,
    @Expose @SerializedName("streetnumber") val streetnumber: String?,
    @Expose @SerializedName("complement") val complement: String?,
    @Expose @SerializedName("zipCode") val zipCode: String?,
    @Expose @SerializedName("plus_code") val plusCode: String?,
    @Expose @SerializedName("contact") val contactName: String?,
    @Expose @SerializedName("phone") val contactPhone: String?,
    @Expose @SerializedName("siteMainUser") val siteMainUser: Int?,
    @Expose @SerializedName("minDate") val minDate: String?,
    @Expose @SerializedName("serialCnt") val serialCnt: Int,
    @Expose @SerializedName("actions") var actions: MutableList<FsTripDestinationAction>? = mutableListOf(),
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
        destinationType: String? = null,
        destinationSiteCode: Int? = null,
        destinationSiteDesc: String? = null,
        destinationRegionCode: Int? = null,
        destinationRegionDesc: String? = null,
        ticketPrefix: Int? = null,
        ticketCode: Int? = null,
        ticketId: String? = null,
        destinationStatus: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        arrivedDate: String? = null,
        arrivedLat: Double? = null,
        arrivedLon: Double? = null,
        arrivedType: String? = null,
        arrivedFleetOdometer: Long? = null,
        arrivedFleetPhoto: String? = null,
        arrivedFleetPhotoName: String? = null,
        arrivedFleetPhotoLocal: String? = null,
        arrivedFleetPhotoChanged: Int = 0,
        departedDate: String? = null,
        departedLat: Double? = null,
        departedLon: Double? = null,
        departedType: String? = null,
        countryId: String? = null,
        state: String? = null,
        city: String? = null,
        district: String? = null,
        street: String? = null,
        streetnumber: String? = null,
        complement: String? = null,
        zipCode: String? = null,
        plusCode: String? = null,
        contactName: String? = null,
        contactPhone: String? = null,
        siteMainUser: Int? = null,
        minDate: String? = null,
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
        arrivedFleetPhotoChanged = arrivedFleetPhotoChanged,
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
            streetnumber?.takeIf { it.isNotEmpty() }?.let { ", $it" } ,
            complement?.takeIf { it.isNotEmpty() }?.let { ", $it" },
            district?.takeIf { it.isNotEmpty() }?.let { ", $it" },
            city?.takeIf { it.isNotEmpty() }?.let { ", $it" },
            state?.takeIf { it.isNotEmpty() }?.let { " - $it" },
            zipCode?.takeIf { it.isNotEmpty() }?.let { ", $it" }
        ).joinToString("").trimIndent()
    }

    fun getAddress(): String {
        return listOfNotNull(
            street,
            streetnumber?.takeIf { it.isNotEmpty() }?.let { ", $it" },
            city?.takeIf { it.isNotEmpty() }?.let { ", $it" },
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

    fun containsContact(): Boolean {
        return listOf(
            contactName,
            contactPhone
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