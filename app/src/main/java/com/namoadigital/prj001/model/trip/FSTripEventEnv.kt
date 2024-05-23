package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName


data class FSTripEventEnv(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("eventSeq") val eventSeq: Int?,
    @SerializedName("eventTypeCode") val eventTypeCode: Int,
    val eventTypeDesc: String,
    @SerializedName("eventStatus") val eventStatus: String,
    @SerializedName("eventCost") val eventCost: Double?,
    @SerializedName("eventComments") val eventComments: String?,
    @SerializedName("eventPhoto") val eventPhoto: String?,
    @SerializedName("eventStart") val eventStart: String?,
    @SerializedName("eventEnd") val eventEnd: String?,
    @SerializedName("changedPhoto") val changedPhoto: Int?
){
    companion object{
        val WS_BUNDLE_KEY = FSTripEventEnv::class.java.name
    }
}