package com.namoadigital.prj001.model.trip.mapping

import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestinationFullUpdateEnv

fun FsTripDestination.toTripUpdate() = FsTripDestinationFullUpdateEnv(
    destinationSeq =this.destinationSeq,
    destinationType = this.destinationType,
    destinationSiteCode = this.destinationSiteCode,
    ticketPrefix = this.ticketPrefix,
    ticketCode = this.ticketCode,
    destinationStatus = this.destinationStatus,
    latitude = this.latitude,
    longitude = this.longitude,
    arrivedDate = this.arrivedDate,
    arrivedLat = this.arrivedLat,
    arrivedLon = this.arrivedLon,
    arrivedType = this.arrivedType,
    arrivedFleetOdometer = this.arrivedFleetOdometer,
    departedDate = this.departedDate,
    departedLat = this.departedLat,
    departedLon = this.departedLon,
    departedType = this.departedType,
    arrivedFleetPhotoChanged = this.arrivedFleetPhotoChanged ?: 0,
    arrivedFleetPhotoKey = this.arrivedFleetPhotoLocal

)