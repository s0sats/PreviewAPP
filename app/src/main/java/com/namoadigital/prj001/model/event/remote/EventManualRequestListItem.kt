package com.namoadigital.prj001.model.event.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventManualRequestListItem(
    @Expose @SerializedName("eventUser") val eventUser: Int,
    @Expose @SerializedName("evenDay") val evenDay: Int
)