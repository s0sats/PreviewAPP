package com.namoadigital.prj001.model.event.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EventManualResponseListItem(
    @Expose @SerializedName("eventUser") var eventUser: Int? = null,
    @Expose @SerializedName("evenDay") var evenDay: Int? = null,
    @Expose @SerializedName("eventDaySeq") var eventDaySeq: Int? = null,
    @Expose @SerializedName("eventTypeCode") var eventTypeCode: Int? = null,
    @Expose @SerializedName("eventTypeDesc") var eventTypeDesc: String? = null,
    @Expose @SerializedName("eventCost") var eventCost: String? = null,
    @Expose @SerializedName("eventComments") var eventComments: String? = null,
    @Expose @SerializedName("eventPhoto") var eventPhoto: String? = null,
    @Expose @SerializedName("eventName") var eventName: String? = null,
    @Expose @SerializedName("eventStart") var eventStart: String? = null,
    @Expose @SerializedName("eventEnd") var eventEnd: String? = null,
    @Expose @SerializedName("eventStatus") var eventStatus: String? = null
)
