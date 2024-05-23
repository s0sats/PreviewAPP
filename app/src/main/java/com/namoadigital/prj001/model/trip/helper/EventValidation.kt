package com.namoadigital.prj001.model.trip.helper

import com.namoadigital.prj001.model.trip.FSTripEvent

data class EventValidation(
    val event:FSTripEvent,
    val waitAllowed: Boolean
)