package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription

class CheckNextDestinationStatusTripUseCase constructor(
    private val repository: TripRepository,
    private val destinationRepository: TripDestinationRepository,
) : UseCaseWithoutFlow<CheckNextDestinationStatusTripUseCase.Input, CheckNextDestinationStatusTripUseCase.Output> {

    data class Input(
        val tripStatus: TripStatus,
    )

    data class Output(
        val nextTripStatus: String,
        val destinationSeq: Int?,
        val destinationStatus: String?,
        val nextDestinationSeq: Int?,
        val nextDestinationStatus: String?,
    )

    override fun invoke(input: Input): Output {

        return when(input.tripStatus){
            TripStatus.PENDING -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.TRANSIT -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.TRANSFER -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.ON_SITE -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.OVER_NIGHT -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.WAITING_DESTINATION -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.CANCELLED -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.START -> {
                val trip = repository.getTrip()
                val destination = trip?.let {
                    destinationRepository.getDestinationByStatus(
                        customerCode = it.customerCode,
                        tripPrefix = it.tripPrefix,
                        tripCode = it.tripCode,
                        DestinationStatus.PENDING,
                    )
                }
                //
                destination?.let {
                    Output(
                        nextTripStatus = TripStatus.TRANSIT.toDescription(),
                        destinationSeq = destination.destinationSeq,
                        destinationStatus = DestinationStatus.TRANSIT.toDescription(),
                        nextDestinationSeq = null,
                        nextDestinationStatus = null,
                    )
                }?: getNullOutput(TripStatus.WAITING_DESTINATION.toDescription())
            }

            TripStatus.DONE -> getNullOutput(input.tripStatus.toDescription())
            TripStatus.NULL -> getNullOutput(input.tripStatus.toDescription())
        }

    }
    //
    private fun getNullOutput(status: String) = Output(status, null, null, null, null)
}