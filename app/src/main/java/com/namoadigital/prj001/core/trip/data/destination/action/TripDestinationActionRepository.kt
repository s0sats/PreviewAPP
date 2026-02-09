package com.namoadigital.prj001.core.trip.data.destination.action

import com.namoadigital.prj001.adapter.trip.model.Extract

import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict

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

    fun checkDateConflict(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        newStart: String,
        newEnd: String?,
        validateStartDateEquals: Boolean = false
    ): EventConflict?

    fun getDestinationFormDateConflict(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        newArrivedDate: String,
        newDepartedDate: String?
    ) : EventConflict?




}