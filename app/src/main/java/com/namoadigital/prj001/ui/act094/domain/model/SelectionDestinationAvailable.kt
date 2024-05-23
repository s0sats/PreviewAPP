package com.namoadigital.prj001.ui.act094.domain.model

import android.content.Context
import com.namoadigital.prj001.util.ToolBox_Inf

data class SelectionDestinationAvailable(
    val destinationType: String?,
    val siteCode: Int?,
    val siteDesc: String?,
    val ticketPrefix: Int?,
    val ticketCode: Int?,
    val serialId: String?,
    val address: Int?,
    val countryId: String?,
    val state: String?,
    val city: String?,
    val district: String?,
    val street: String?,
    val streetnumber: String?,
    val complement: String?,
    val zipCode: String?,
    val plusCode: String?,
    val lat: Double?,
    val lon: Double?,
    val contactName: String?,
    val contactPhone: String?,
    val siteMainUser: Int?,
    val regionCode: Int?,
    val regionDesc: String?,
    val minDate: String?,
    val priorityCnt: Int?,
    val todayCnt: Int?,
    val lateCnt: Int?,
    val nextCnt: Int?,
    val serialCnt: Int?
) {
    fun getFullAddress(): String {
        return listOfNotNull(
            street,
            streetnumber?.let { ", $it" },
            complement?.let { ", $it" },
            district?.let { ", $it" },
            city?.let { ", $it" },
            state?.let { "- $it" },
            zipCode
        ).joinToString("").trimIndent()
    }
    fun getMapsAddress(): String {
        return listOfNotNull(
            street,
            streetnumber?.let { ", $it" },
            city?.let { ", $it" }
        ).joinToString("").trimIndent()
    }


    fun getAllFieldForFilter(context: Context, externalAddress: String) =
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
            zipCode,
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(minDate),
                ToolBox_Inf.nlsDateFormat(context)
            ),
            externalAddress
        ).joinToString("|")


    companion object {
        fun Int?.convertZeroToLine(): String{
            return try {
                if(this == 0 || this == null) "-" else "$this"
            }catch (e: Exception){
                "$this"
            }
        }
    }

}