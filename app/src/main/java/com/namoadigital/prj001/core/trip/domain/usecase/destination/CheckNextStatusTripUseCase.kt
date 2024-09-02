package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toTripStatus

class CheckNextStatusTripUseCase constructor(
    private val repository: TripRepository,
    private val destinationRepository: TripDestinationRepository,
) : UseCaseWithoutFlow<DestinationStatus, TripStatus> {

    override fun invoke(input: DestinationStatus): TripStatus {
        val trip = repository.getTrip()
        val tripStatus = trip?.tripStatus?.toTripStatus() ?: TripStatus.NULL
        val nextDestination = destinationRepository.getDestinationByStatus(
            customerCode = trip?.customerCode ?: -1,
            tripPrefix = trip?.tripPrefix ?: -1,
            tripCode = trip?.tripCode ?: -1,
            DestinationStatus.PENDING
        )


        val statusCancelledList = listOf(TripStatus.TRANSIT, TripStatus.TRANSFER)
        val statusDepartedList = listOf(TripStatus.ON_SITE, TripStatus.OVER_NIGHT)

        return when (input) {
            DestinationStatus.CANCELLED -> {
                if (statusCancelledList.contains(tripStatus)) {
                    TripStatus.WAITING_DESTINATION
                } else {
                    tripStatus
                }
            }

            DestinationStatus.ARRIVED -> {
                when (tripStatus) {
                    TripStatus.TRANSIT -> {
                        TripStatus.ON_SITE
                    }
                    TripStatus.OVER_NIGHT -> {
                        if (nextDestination != null
                            && nextDestination.destinationStatus == DestinationStatus.PENDING.toDescription()) {
                            TripStatus.TRANSIT
                        }else{
                            TripStatus.WAITING_DESTINATION
                        }
                    }
                    else -> {
                        tripStatus
                    }
                }
            }

            DestinationStatus.DEPARTED -> {
                if (statusDepartedList.contains(tripStatus)) {
                    if (nextDestination != null) {
                        TripStatus.TRANSIT
                    } else {
                        TripStatus.WAITING_DESTINATION
                    }
                } else {
                    tripStatus
                }
            }

            DestinationStatus.PENDING -> {
                if (nextDestination != null) {
                    TripStatus.TRANSIT
                } else {
                    TripStatus.WAITING_DESTINATION
                }
            }
            else -> {
                TripStatus.NULL
            }
        }

    }


}