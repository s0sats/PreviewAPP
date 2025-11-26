package com.namoadigital.prj001.model.event.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.extensions.toBoolean
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus

data class EventManualSetRequestItem(
    @Expose @SerializedName("appId") val appId: String? = null,
    @Expose @SerializedName("eventUser") val eventUser: Int,
    @Expose @SerializedName("eventDay") val eventDay: Int,
    @Expose @SerializedName("eventDaySeq") val eventDaySeq: Int? = null,
    @Expose @SerializedName("eventTypeCode") val eventTypeCode: Int,
    val description: String,
    @Expose @SerializedName("eventCost") val eventCost: String? = null,
    @Expose @SerializedName("eventComments") val eventComments: String? = null,
    @Expose @SerializedName("eventPhoto") val eventPhoto: String? = null,
    @Expose @SerializedName("eventName") val eventName: String? = null,
    @Expose @SerializedName("eventStart") val eventStart: String,
    @Expose @SerializedName("eventEnd") val eventEnd: String? = null,
    @Expose @SerializedName("changedPhoto") val changedPhoto: Int = 0,
    @Expose @SerializedName("eventStatus") val eventStatus: String
) {
    fun toEntity(
        updateRequired: Boolean = false
    ) = EventManual(
        appId = appId,
        user = eventUser,
        eventDay = eventDay,
        daySeq = eventDaySeq,
        typeCode = eventTypeCode,
        description = description,
        cost = eventCost,
        comments = eventComments,
        photo = EventManual.Photo(
            url = eventName,
            name = eventPhoto,
            local = eventPhoto,
            isChanged = changedPhoto.toBoolean()
        ),
        dateStart = eventStart,
        dateEnd = eventEnd,
        status = EventStatus.valueOf(eventStatus),
        isUpdateRequired = updateRequired
    )

}
