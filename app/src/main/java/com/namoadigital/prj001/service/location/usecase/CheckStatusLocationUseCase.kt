package com.namoadigital.prj001.service.location.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.model.trip.toTripStatus

class CheckStatusLocationUseCase(
    private val tripRepository: TripRepository,
    private val destinationRepository: TripDestinationRepository,
    private val positionUndefinedDestinationUseCase: PositionUndefinedDestinationUseCase,
    private val positionDefinedDestinationUseCase: PositionDefinedDestinationUseCase,
    private val currentTripPref: CurrentTripPref
) : UseCaseWithoutFlow<Coordinates, Unit> {

    override fun invoke(input: Coordinates) {
        val trip = tripRepository.getTrip()
        val status = trip?.tripStatus?.toTripStatus() ?: TripStatus.NULL
        val currentDate = getCurrentDateApi(true)
        input.date = currentDate
        val tripPrefModel = currentTripPref.read()


        setWaitingDestinationTimerForWarning(status, tripPrefModel, input)

        when {
            isPendingOrTransferOrWaitingDestination(status) -> {
                val destination = destinationRepository.getDestinationByStatus(
                    trip?.customerCode ?: -1,
                    trip?.tripPrefix ?: -1,
                    trip?.tripCode ?: -1,
                    DestinationStatus.PENDING
                )
                if (trip?.tripStatus == TripStatus.PENDING.toDescription()) {
                    handleDestinationNotDefined(input)
                } else {
                    destination?.let {
                        it.destinationStatus?.let { status ->
                            if (isPendingDestination(status.toDestinationStatus())) {
                                handleDestinationPending(input)
                            }
                        }
                    } ?: handleDestinationNotDefined(input)
                }
            }
            //
            isOnSiteOrOverNight(status) -> {
                val destination = destinationRepository.getDestinationByStatus(
                    trip?.customerCode ?: -1,
                    trip?.tripPrefix ?: -1,
                    trip?.tripCode ?: -1,
                    DestinationStatus.ARRIVED
                )
                destination?.let {
                    handleDestinationArrived(input, destination)
                }
            }
            //
            isTransit(status) -> {
                val destination = destinationRepository.getDestinationByStatus(
                    trip?.customerCode ?: -1,
                    trip?.tripPrefix ?: -1,
                    trip?.tripCode ?: -1,
                    DestinationStatus.TRANSIT
                )
                destination?.let {
                    handleDestinationTransit(input, destination)
                }
            }
        }
    }

    private fun setWaitingDestinationTimerForWarning(
        status: TripStatus,
        tripPrefModel: CurrentTripPrefModel,
        input: Coordinates
    ) {

        if (status == TripStatus.WAITING_DESTINATION) {
            if (tripPrefModel.waiting_destination_date == null) {
                currentTripPref.write(
                    tripPrefModel.copy(
                        waiting_destination_date = input.date
                    )
                )
            }
        } else {
            currentTripPref.write(
                tripPrefModel.copy(
                    waiting_destination_date = null
                )
            )
        }
    }

    private fun handleDestinationTransit(input: Coordinates, destination: FsTripDestination) {
        positionDefinedDestinationUseCase(
            PositionDefinedDestinationUseCase.Params(
                coordinates = input,
                TripPositionAlertType.ARRIVED,
                destination.destinationSeq
            )
        )
    }

    private fun handleDestinationPending(input: Coordinates) {
        positionDefinedDestinationUseCase(
            PositionDefinedDestinationUseCase.Params(
                coordinates = input,
                TripPositionAlertType.DEPARTED,
                null
            )
        )
    }

    private fun handleDestinationArrived(input: Coordinates, destination: FsTripDestination) {
        positionDefinedDestinationUseCase(
            PositionDefinedDestinationUseCase.Params(
                coordinates = input,
                TripPositionAlertType.DEPARTED,
                destination.destinationSeq
            )
        )
    }

    private fun handleDestinationNotDefined(input: Coordinates) {
        positionUndefinedDestinationUseCase(
            PositionUndefinedDestinationUseCase.Params(
                input,
                TripPositionAlertType.DEPARTED,
                null
            )
        )
    }

    private fun isPending(status: TripStatus) = status == TripStatus.PENDING
    private fun isPendingOrTransferOrWaitingDestination(status: TripStatus) =
        status == TripStatus.PENDING || status == TripStatus.TRANSFER || status == TripStatus.WAITING_DESTINATION

    private fun isOnSiteOrOverNight(status: TripStatus) =
        status == TripStatus.ON_SITE || status == TripStatus.OVER_NIGHT

    private fun isTransit(status: TripStatus) = status == TripStatus.TRANSIT
    private fun isTransitDestination(status: DestinationStatus) =
        status == DestinationStatus.TRANSIT

    private fun isNotDefined(destinationStatus: DestinationStatus): Boolean {
        listOf(
            DestinationStatus.PENDING,
            DestinationStatus.ARRIVED,
            DestinationStatus.TRANSIT,
        ).let { list ->
            return !list.contains(destinationStatus)
        }
    }

    private fun isArrived(destinationStatus: DestinationStatus) =
        destinationStatus == DestinationStatus.ARRIVED

    private fun isPendingDestination(destinationStatus: DestinationStatus) =
        destinationStatus == DestinationStatus.PENDING

}
