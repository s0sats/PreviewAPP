package com.namoadigital.prj001.ui.act005.trip.util

import com.namoadigital.prj001.model.trip.DestinationCounter
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.toTripStatus

data class TripState(
    val trip: FSTrip? = null,
    val destination: FsTripDestination? = null,
    val counter: DestinationCounter? = null,
    val event: FSTripEvent? = null,
){
    val containsEvent = event != null
    val tripStatus = trip?.tripStatus?.toTripStatus()
}