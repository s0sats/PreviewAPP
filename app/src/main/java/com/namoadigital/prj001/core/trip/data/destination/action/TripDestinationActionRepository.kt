package com.namoadigital.prj001.core.trip.data.destination.action

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestinationAction

interface TripDestinationActionRepository {

    fun getDestinationActions(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
    ): List<FsTripDestinationAction>

    fun getExtract(trip: FSTrip?): List<Extract<FsTripDestinationAction>>
    fun getFormInProcessExtract(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<TripSiteExtract<GE_Custom_Form_Local>>

    fun getDestinationActionExtract(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<TripSiteExtract<FsTripDestinationAction>>
}