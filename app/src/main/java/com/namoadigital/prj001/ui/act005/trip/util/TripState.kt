package com.namoadigital.prj001.ui.act005.trip.util

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.model.trip.DestinationCounter
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.toTripStatus

data class TripState(
    val trip: FSTrip? = null,
    val destination: FsTripDestination? = null,
    val counter: DestinationCounter? = null,
    val event: FSTripEvent? = null,
    var updateTripScreen: Boolean = false,
    val listExtract: List<Extract<*>>? = null,
    val progressState: ProgressState = ProgressState.Hide(true),
) {
    val containsEvent = event != null
    val tripStatus = trip?.tripStatus?.toTripStatus()
}