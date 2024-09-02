package com.namoadigital.prj001.model.trip.mapping

import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripFullUpdateEnv

fun FSTrip.toTripUpdate() = FSTripFullUpdateEnv(
    customerCode = this.customerCode,
    tripPrefix = this.tripPrefix,
    tripCode = this.tripCode,
    scn = this.scn,
    tripStatus = this.tripStatus,
    fleetLicencePlate = this.fleetLicencePlate,
    fleetStartOdometer = this.fleetStartOdometer,
    fleetEndOdometer = this.fleetEndOdometer,
    originType = this.originType,
    originSiteCode = this.originSiteCode,
    originLat = this.originLat,
    originLon = this.originLon,
    originDate = this.originDate,
    doneDate = this.doneDate,
    fleetStartPhotoChanged = this.fleetStartPhotoChanged,
    fleetStartPhotoKey = this.fleetStartPhotoLocal,
    fleetEndPhotoChanged = this.fleetEndPhotoChanged,
    fleetEndPhotoKey = this.fleetEndPhotoLocal,
    users = this.users?.map { user -> user.toTripUpdate() },
    events = this.events?.map { event -> event.toTripUpdate() },
    destinations = this.destinations?.map { destination -> destination.toTripUpdate() }
)