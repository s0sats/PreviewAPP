package com.namoadigital.prj001.model.trip.mapping

import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripEventFullUpdateEnv

fun FSTripEvent.toTripUpdate() = FSTripEventFullUpdateEnv(
    eventSeq = this.eventSeq,
    eventTypeCode = this.eventTypeCode,
    eventCost = this.cost,
    eventComments = this.comment,
    eventStatus = this.eventStatus ?: "",
    eventStart = this.eventStart,
    eventEnd = this.eventEnd,
    eventPhotoChanged = this.eventPhotoChanged,
    eventPhotoKey = this.photoLocal
)