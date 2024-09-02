package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class FSTripEventEnv(
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int,
    @Expose @SerializedName("tripCode") var tripCode: Int,
    @Expose @SerializedName("scn") var scn: Int,
    @Expose @SerializedName("eventSeq") val eventSeq: Int?,
    @Expose @SerializedName("eventTypeCode") val eventTypeCode: Int,
    val eventTypeDesc: String,
    @Expose @SerializedName("eventStatus") val eventStatus: String,
    @Expose @SerializedName("eventCost") val eventCost: Double?,
    @Expose @SerializedName("eventComments") val eventComments: String?,
    @Expose @SerializedName("eventPhoto") val eventPhoto: String?,
    @Expose @SerializedName("eventStart") val eventStart: String?,
    @Expose @SerializedName("eventEnd") val eventEnd: String?,
    @Expose @SerializedName("changedPhoto") val changedPhoto: Int
){
    companion object{
        val WS_BUNDLE_KEY = FSTripEventEnv::class.java.name
    }
}