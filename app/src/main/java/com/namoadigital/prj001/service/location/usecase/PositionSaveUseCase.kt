package com.namoadigital.prj001.service.location.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.service.location.util.calculateDistance


class PositionSaveUseCase constructor(
    private val positionRepository: TripPositionRepository,
    private val tripRepository: TripRepository,
    private val destinationRepository: TripDestinationRepository,
) : UseCaseWithoutFlow<PositionSaveUseCase.Params, Unit> {
    data class Params(
        val coordinates: Coordinates,
        val alertType: TripPositionAlertType?,
        val destinationSeq: Int?,
        val isRef: Int?,
    )
    override fun invoke(input: Params) {
        val currentLocation = input.coordinates
        val trip = tripRepository.getTrip()
        trip?.let{
            //
            val origin = Coordinates(
                it.originLat?: 0.0,
                it.originLon?: 0.0,
                it.originDate
            )
            //
            positionRepository.savePosition(
                trip = it,
                coordinates = currentLocation,
                updateRequired = input.isRef?:0,
                alertType = input.alertType,
                distance = getDistance(
//                    input.alertType,
//                    it.tripStatus.toTripStatus(),
                    currentLocation,
                    origin,
//                    destination,
                    positionRepository.getLocationSave()
                ),
                tripDestinationSeq = input.destinationSeq,
                isRef = input.isRef?:0
            )
        }
    }

    private fun getDistance(
//        alertType: TripPositionAlertType?,
//        tripStatus: TripStatus?,
        currentLocation: Coordinates,
        origin: Coordinates?,
//        destination: Coordinates?,
        saveLocation: Coordinates?
    ) : Double? {
        return saveLocation?.let {
            calculateDistance(it, currentLocation)
        }?: run{
            origin?.let {
                calculateDistance(currentLocation, it)
            }
        }
//        return when(alertType){
//            TripPositionAlertType.ARRIVED -> {
//                destination?.let {
//                    calculateDistance(it, currentLocation)
//                }
//            }
//            TripPositionAlertType.DEPARTED -> {
//
//                if(tripStatus == TripStatus.PENDING){
//                    origin?.let {
//                        calculateDistance(currentLocation, it)
//                    }
//                }else{
//                    destination?.let {
//                        calculateDistance(it, currentLocation)
//                    }
//                }
//
//            }
//            TripPositionAlertType.WAITING_DESTINATION -> {
//                calculateDistance(saveLocation!!, currentLocation)
//            }
//            else -> {
//                saveLocation?.let {
//                    calculateDistance(it, currentLocation)
//                }
//            }
//        }
    }

}