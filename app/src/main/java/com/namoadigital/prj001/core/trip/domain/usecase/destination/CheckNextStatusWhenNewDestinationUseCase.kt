package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.service.location.FsTripLocationService

class CheckNextStatusWhenNewDestinationUseCase constructor(
    private val repository: TripDestinationRepository,
    private val tripRepository: TripRepository
) : UseCaseWithoutFlow<CheckNextStatusWhenNewDestinationUseCase.Params, CheckNextStatusWhenNewDestinationUseCase.Output> {

    data class Params(
        val tripPrefix: Int,
        val tripCode: Int,
        val destinationType: String? = null
    )

    data class Output(
        val destinationSeq: Int? = null,
        val tripStatus: TripStatus? = null,
        val destinationStatus: DestinationStatus? = null,
        val date: String? = null,
        val lat: Double? = null,
        val lon: Double? = null,
        val arrivedLat: Double? = null,
        val arrivedLon: Double? = null,
    )

    override fun invoke(input: Params): Output {
        tripRepository.getTrip()?.let { trip ->
            val (tripPrefix, tripCode, destinationType) = input
            val tripStatus = trip.tripStatus.toTripStatus()
            val destinationSeq = repository.getNextDestinationSeq(tripPrefix, tripCode)
            val checkStatus = listOf(TripStatus.WAITING_DESTINATION, TripStatus.TRANSFER)

            if (destinationSeq == null) {
                return Output()
            }

            return when {

                destinationType == FsTripDestination.OVER_NIGHT_DESTINATION_TYPE -> {
                    Output(
                        destinationSeq = destinationSeq,
                        tripStatus = TripStatus.OVER_NIGHT,
                        destinationStatus = DestinationStatus.ARRIVED,
                        date = getCurrentDateApi(),
                        arrivedLat = FsTripLocationService.LatLog.value.latitude,
                        arrivedLon = FsTripLocationService.LatLog.value.longitude,
                    )
                }

                checkStatus.contains(tripStatus) -> {
                    Output(
                        destinationSeq = destinationSeq,
                        tripStatus = TripStatus.TRANSIT,
                        destinationStatus = DestinationStatus.TRANSIT
                    )
                }

//                tripStatus == TripStatus.OVER_NIGHT  -> {
//                    Output(
//                        destinationSeq = destinationSeq ?: 1,
//                        tripStatus = TripStatus.OVER_NIGHT,
//                        destinationStatus = DestinationStatus.PENDING,
//                    )
//                }

                else -> {
                    Output(
                        destinationSeq = destinationSeq ?: 1,
                        tripStatus = tripStatus,
                        destinationStatus = DestinationStatus.PENDING
                    )
                }

            }
        } ?: return Output()
    }
}