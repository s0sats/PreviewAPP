package com.namoadigital.prj001.core.trip.domain.model

import com.namoadigital.prj001.model.trip.FsTripDestination

data class OdometerArrivedDestination(
    val date: String,
    val odometer: Long
){

    companion object {

        fun FsTripDestination.toOdometerList() = OdometerArrivedDestination(
            date = this.arrivedDate ?: "",
            odometer = this.arrivedFleetOdometer ?: 0
        )

    }

}