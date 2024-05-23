package com.namoadigital.prj001.model.trip.mapping

import com.namoadigital.prj001.core.trip.domain.model.UserPositionEnv
import com.namoadigital.prj001.model.trip.FsTripPosition

fun FsTripPosition.toEnv() = UserPositionEnv(
    tripPrefix = this.tripPrefix,
    tripCode = this.tripCode,
    lat = this.tripPositionLat ?: 0.0,
    lon = this.tripPositionLon?: 0.0,
    gpsDate = this.tripPositionDate ?: "",
    destinationSeq = this.tripDestinationSeq,
    alert = this.tripPositionAlertType ?: "",
    speed = this.tripPositionSpeed,
    isRef = this.isRef
)