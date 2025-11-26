package com.namoadigital.prj001.core.trip.data.destination

import android.os.Bundle
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import kotlinx.coroutines.flow.Flow

interface TripDestinationRepository {

    suspend fun getListExternalAddress() : List<DestinationAvailables>
    suspend fun getListSiteAddress(): List<DestinationAvailables>
    fun execServiceSelectDestination(bundle: Bundle)
    fun execServiceOvernightDestination(
        trip:FSTrip?,
        destinationType: String,
        currentLat: Double?=null,
        currentLon: Double?=null,
    ):Flow<IResult<Unit>>
    fun saveFilterPreference(filter: AvailableDestinationFilter)
    fun getDestinationFilterPreference(): AvailableDestinationFilter

    fun getDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): FsTripDestination?


    fun getDestinationByStatus(customerCode: Long, tripPrefix: Int, tripCode:Int, status: DestinationStatus): FsTripDestination?

    fun getLastDestinationStatus(
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
        remoteDestination: SelectDestinationRec,
        destination: SelectionDestinationAvailable,
        isOnlineFLow:Boolean = false
    ):Boolean
    fun saveOverNightDestination(
        customerCode: Long,
        remoteDestination: SelectDestinationRec,
        isOnline: Boolean= true
    ):Boolean

    suspend fun setTripDestinationStatusChange(
        destinationSeq: Int,
        status: String,
        tripStatus: String
    ): Flow<IResult<Unit>>

    fun getExtract(trip: FSTrip?): List<Extract<FsTripDestination>>

    fun getListOdometerArrived() : List<OdometerArrivedDestination>
    fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int
    ): Flow<IResult<Unit>>

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

    fun getNextDestinationSeq(tripPrefix: Int, tripCode: Int): Int?

    fun getLastDestination(prefix: Int, code: Int): FsTripDestination?

}