package com.namoadigital.prj001.model.event.local

import com.namoadigital.prj001.extensions.toInt
import com.namoadigital.prj001.model.event.remote.EventManualSetRequestItem
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData.EventFieldConfig

data class EventManual(
    val appId: String? = null,
    val user: Int,
    val eventDay: Int,
    val daySeq: Int? = null,
    val typeCode: Int,
    val description: String,
    val cost: String? = null,
    val comments: String? = null,
    val photo: Photo = Photo(),
    val dateStart: String,
    val dateEnd: String? = null,
    val status: EventStatus,
    val eventSiteCode: Int?,
    val eventSiteDesc: String? = null,
    val isUpdateRequired: Boolean = false,
) {

    var eventFieldConfig: EventFieldConfig? = EventFieldConfig()


    data class Photo(
        val url: String? = null,
        val name: String? = null,
        val local: String? = null,
        val isChanged: Boolean = false
    ) {
        fun hasPhoto() = url != null || local != null
    }

    /**
     * Conversão de [EventManual] (camada local) para [EventManualData] (domínio)
     */
    fun toScreenData(): EventManualData = EventManualData(
        primaryData = EventManualData.PrimaryData(
            appId = appId,
            typeCode = typeCode,
            eventDaySeq = daySeq,
            eventUser = user,
            eventDay = eventDay
        ),
        title = description,
        cost = cost,
        comment = comments,
        status = status,
        startDate = dateStart,
        endDate = dateEnd,
        photo = EventManualData.PhotoData(
            url = photo.url,
            name = photo.name,
            localPath = photo.local,
            isChangedPhoto = photo.isChanged
        ),
        eventFieldConfig = eventFieldConfig!!,
        eventSiteCode = eventSiteCode,
        eventSite = eventSiteDesc,
    )

    fun toDomain(): EventManualSetRequestItem = EventManualSetRequestItem(
        appId = appId,
        eventUser = user,
        eventDay = eventDay,
        eventDaySeq = daySeq,
        eventTypeCode = typeCode,
        eventCost = cost,
        eventComments = comments,
        eventPhoto = photo.local,
        eventName = photo.url,
        eventStart = dateStart,
        eventEnd = dateEnd,
        changedPhoto = photo.isChanged.toInt(),
        eventStatus = status.name,
        description = description,
        eventSiteCode = eventSiteCode,
    )
}