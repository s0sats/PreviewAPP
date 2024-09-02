package com.namoadigital.prj001.ui.act094.destination.domain.destination_availables

import android.content.Context
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.util.ToolBox_Inf

data class DestinationAvailables(
    @Expose @SerializedName("destinationType") val destinationType: String? = null,
    @Expose @SerializedName("siteCode") val siteCode: Int? = null,
    @Expose @SerializedName("siteDesc") val siteDesc: String? = null,
    @Expose @SerializedName("ticketPrefix") val ticketPrefix: Int? = null,
    @Expose @SerializedName("ticketCode") val ticketCode: Int? = null,
    @Expose @SerializedName("serialId") val serialId: String? = null,
    @Expose @SerializedName("address") val address: Int? = null,
    @Expose @SerializedName("countryId") val countryId: String? = null,
    @Expose @SerializedName("state") val state: String? = null,
    @Expose @SerializedName("city") val city: String? = null,
    @Expose @SerializedName("district") val district: String? = null,
    @Expose @SerializedName("street") val street: String? = null,
    @Expose @SerializedName("streetnumber") val streetnumber: String? = null,
    @Expose @SerializedName("complement") val complement: String? = null,
    @Expose @SerializedName("zipcode") val zipcode: String? = null,
    @Expose @SerializedName("plus_code") val plusCode: String? = null,
    @Expose @SerializedName("latitude") val lat: Double? = null,
    @Expose @SerializedName("longitude") val lon: Double? = null,
    @Expose @SerializedName("contact") val contactName: String? = null,
    @Expose @SerializedName("phone") val contactPhone: String? = null,
    @Expose @SerializedName("siteMainUser") val siteMainUser: Int? = null,
    @Expose @SerializedName("regionCode") val regionCode: Int? = null,
    @Expose @SerializedName("regionDesc") val regionDesc: String? = null,
    @Expose @SerializedName("minDate") val minDate: String? = null,
    @Expose @SerializedName("priorityCnt") val priorityCnt: Int? = null,
    @Expose @SerializedName("todayCnt") val todayCnt: Int? = null,
    @Expose @SerializedName("lateCnt") val lateCnt: Int? = null,
    @Expose @SerializedName("nextCnt") val nextCnt: Int? = null,
    @Expose @SerializedName("serialCnt") val serialCnt: Int? = null,
) {


    fun formatToAddress(): String {
        val builder = StringBuilder()

        if (!street.isNullOrEmpty()) builder.append(street)
        if (!streetnumber.isNullOrEmpty()) builder.append(", $streetnumber")
        if (!complement.isNullOrEmpty()) builder.append(" - $complement")
        if (!district.isNullOrEmpty()) builder.append(" - $district")
        if (!city.isNullOrEmpty()) builder.append(", $city")
        if (!state.isNullOrEmpty()) builder.append(" - $state")
        if (!zipcode.isNullOrEmpty()) builder.append(", $zipcode")

        return builder.toString()
    }

    fun getAllFieldForFilter(context: Context) =
        listOf(
            serialId,
            siteDesc,
            regionDesc,
            street,
            streetnumber,
            complement,
            district,
            city,
            state,
            zipcode,
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(minDate),
                ToolBox_Inf.nlsDateFormat(context)
            ),
        ).joinToString("|")

}