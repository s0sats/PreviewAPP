package com.namoadigital.prj001.model.event.remote

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus

data class EventManualSync(
    @SerializedName("event_user") var eventUser: Int,
    @SerializedName("event_day") var eventDay: Int,
    @SerializedName("event_day_seq") var eventDaySeq: Int? = null,
    @SerializedName("event_status") var eventStatus: String? = null,
    @SerializedName("event_type_code") var eventTypeCode: Int,
    @SerializedName("event_type_desc") var eventTypeDesc: String? = null,
    @SerializedName("event_cost") var eventCost: String? = null,
    @SerializedName("event_comments") var eventComments: String? = null,
    @SerializedName("event_photo") var eventPhoto: String? = null,
    @SerializedName("event_photo_name") var eventPhotoName: String? = null,
    @SerializedName("event_start") var eventStart: String? = null,
    @SerializedName("event_end") var eventEnd: String? = null
) {

    fun toEntity(): EventManual {
        return EventManual(
            user = eventUser,
            eventDay = eventDay,
            daySeq = eventDaySeq,
            typeCode = eventTypeCode,
            description = eventTypeDesc ?: "",
            cost = eventCost,
            comments = eventComments,
            photo = EventManual.Photo(
                url = eventPhoto,
                name = eventPhotoName,
                local = eventPhotoName
            ),
            dateStart = eventStart ?: "",
            dateEnd = eventEnd,
            status = EventStatus.valueOf(eventStatus ?: ""),
            isUpdateRequired = false
        )
    }

}