package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSTripEventFullUpdateEnv(
    @Expose @SerializedName("eventSeq") val eventSeq: Int?,
    @Expose @SerializedName("eventTypeCode") val eventTypeCode: Int,
    @Expose @SerializedName("eventCost") val eventCost: Double?,
    @Expose @SerializedName("eventComments") val eventComments: String?,
    @Expose @SerializedName("eventStatus") val eventStatus: String,
    @Expose @SerializedName("eventStart") val eventStart: String?,
    @Expose @SerializedName("eventEnd") val eventEnd: String?,
    @Expose @SerializedName("eventPhotoChanged") val eventPhotoChanged: Int,
    @Expose @SerializedName("eventPhotoKey") val eventPhotoKey: String?,
)