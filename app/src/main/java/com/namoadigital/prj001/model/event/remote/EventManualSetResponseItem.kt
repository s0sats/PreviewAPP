package com.namoadigital.prj001.model.event.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventManualSetResponseItem(
    @Expose @SerializedName("appId") var appId: String? = null,
    @Expose @SerializedName("eventUser") var eventUser: Int? = null,
    @Expose @SerializedName("evenDay") var evenDay: Int? = null,
    @Expose @SerializedName("eventDaySeq") var eventDaySeq: Int? = null,
    @Expose @SerializedName("eventPhoto") var eventPhoto: String? = null,
    @Expose @SerializedName("eventName") var eventName: String? = null,
    @Expose @SerializedName("eventStatus") var eventStatus: String? = null
)