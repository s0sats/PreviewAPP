package com.namoadigital.prj001.core.trip.data.destination

import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable

interface TripDestinationRepository {

    fun execServiceAvailableDestination(bundle: Bundle)
    fun execServiceSelectDestination(bundle: Bundle)
    fun saveFilterPreference(filter: AvailableDestinationFilter)
    fun getDestinationFilterPreference(): AvailableDestinationFilter

    fun getDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): FsTripDestination?
    fun getTripDestinations(customerCode: Long, tripPrefix: Int, tripCode:Int): List<FsTripDestination>
    fun getTripLastDestinationCoordinate(customerCode: Long, tripPrefix: Int, tripCode:Int): Coordinates?
    fun getDestinationByStatus(customerCode: Long, tripPrefix: Int, tripCode:Int, status: DestinationStatus): FsTripDestination?

    fun getDestinationStatus(
        tripPrefix: Int,
        tripCode: Int
    ): DestinationStatus

    fun getCoordinatesDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): Coordinates?

    fun saveDestination(
        customerCode: Long,
        response: String?,
        destination: SelectionDestinationAvailable
    ):Boolean
    fun saveOverNightDestination(
        customerCode: Long,
        remoteDestination: SelectDestinationRec,
    ):Boolean

    fun setTripDestinationStatusChange(input: TripDestinationStatusChangeEnv)
    fun getExtract(trip: FSTrip?): List<Extract<FsTripDestination>>
    fun getListOdometerArrived() : List<OdometerArrivedDestination>
    fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int
    )

    fun getPreviousValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination?

    fun getNextValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination?

}