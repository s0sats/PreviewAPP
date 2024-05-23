package com.namoadigital.prj001.service.location.usecase

import android.util.Log
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.date.DATE_FORMAT_DEFAULT
import com.namoadigital.prj001.extensions.date.calculateMinutesBetweenDates
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.service.location.util.calculateDistance

class PositionUndefinedDestinationUseCase constructor(
    private val tripRepository: TripRepository,
    private val tripDestinationRepository: TripDestinationRepository,
    private val positionRepository: TripPositionRepository,
    private val positionSaveUseCase: PositionSaveUseCase,
    private val positionCalculateDistanceAndSaveUseCase: PositionCalculateDistanceAndSaveUseCase,
    private val currentTripPref: CurrentTripPref,
) : UseCaseWithoutFlow<PositionUndefinedDestinationUseCase.Params, Unit> {

    data class Params(
        val coordinates: Coordinates,
        val alertType: TripPositionAlertType,
        val tripDestinationSeq: Int?,
    )
    override fun invoke(input: Params) {
        val date = input.coordinates.date!!
        val tripStatus = tripRepository.getTripStatus()
        val trip = tripRepository.getTrip()
        val coordinatesRef = positionRepository.getLocationRefSave()
        if(coordinatesRef == null) {
            trip?.let{
                var isRef = 1
                val positionCounter = 0
                //
                val prefModel = currentTripPref.read()
                currentTripPref.write(
                    prefModel.copy(
                        customer_code = trip.customerCode,
                        trip_prefix = trip.tripPrefix,
                        trip_code = trip.tripCode,
                        trip_scn = trip.scn,
                        destination_seq = input.tripDestinationSeq,
                        position_seq = -1,
                        last_latitude = input.coordinates.latitude,
                        last_longitude = input.coordinates.longitude,
                        last_location_date = input.coordinates.date,
                        last_alert_type = null,
                        position_counter = positionCounter,
                        isRef = isRef,
                        ref_latitude = input.coordinates.latitude,
                        ref_longitude = input.coordinates.longitude,
                        ref_location_date = input.coordinates.date,
                    )
                )
            }
            positionSaveUseCase(
                    PositionSaveUseCase.Params(
                    coordinates = input.coordinates,
                    alertType = null,
                    destinationSeq = input.tripDestinationSeq,
                    1,
                )
            )

            return
        }
        //
        Log.d("TRIP_SERVICE", "------TripPositionAlertType-------")


        val alertType:TripPositionAlertType = when(tripStatus){
            TripStatus.PENDING -> {
                trip?.let{
                    val departedFromOriginDistance =
                        calculateDistance(input.coordinates, it.originCoordinates)
                    if(departedFromOriginDistance > (it.positionDistanceMin ?: 0.1)){
                        TripPositionAlertType.DEPARTED
                    }else{
                        TripPositionAlertType.NULL
                    }
                }?:TripPositionAlertType.NULL
            }
            TripStatus.WAITING_DESTINATION -> {
                val prefModel = currentTripPref.read()
                val differenceMin = prefModel.waiting_destination_date?.let{
                    calculateMinutesBetweenDates(it, date, DATE_FORMAT_DEFAULT)
                }?:0L
                Log.d("TRIP_SERVICE", "differenceMin: $differenceMin")
                if (differenceMin >= 10) {
                    TripPositionAlertType.WAITING_DESTINATION
                }else{
                    TripPositionAlertType.NULL
                }
            }
            else -> TripPositionAlertType.NULL
        }
        //
        Log.d("TRIP_SERVICE", "alertTypeAux: $alertType")
        positionRepository.changeStatusNotification(alertType)
        //
        positionCalculateDistanceAndSaveUseCase(
            PositionCalculateDistanceAndSaveUseCase.Params(
                input.coordinates,
                alertType,
                input.tripDestinationSeq
            )
        )
    }
}