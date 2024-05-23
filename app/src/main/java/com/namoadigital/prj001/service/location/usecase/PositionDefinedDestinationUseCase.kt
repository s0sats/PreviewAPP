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
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.service.location.util.calculateDistance
import javax.inject.Inject

class PositionDefinedDestinationUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val positionRepository: TripPositionRepository,
    private val destinationRepository: TripDestinationRepository,
    private val positionSaveUseCase: PositionSaveUseCase,
    private val currentTripPref: CurrentTripPref
) : UseCaseWithoutFlow<PositionDefinedDestinationUseCase.Params, Unit> {

    data class Params(
        val coordinates: Coordinates,
        val alertType: TripPositionAlertType,
        val tripDestinationSeq: Int?
    )

    override fun invoke(input: Params) {
        val (coordinates, alertType) = input
        val date = coordinates.date!!
        val trip = tripRepository.getTrip()
        val savedDate = positionRepository.getSavedDate()
        val positionDistanceMin = tripRepository.getPositionDistanceMin()
        val prevAlertType = positionRepository.getLastAlertType() ?: ""
        val distance: Double? = trip?.let {
            if (it.tripStatus.toTripStatus() == TripStatus.PENDING) {
                calculateDistance(
                    it.originCoordinates,
                    coordinates
                )
            } else {
                input.tripDestinationSeq?.let { destinationSeq ->
                    val coordinatesDestination = destinationRepository.getCoordinatesDestination(
                        trip.customerCode,
                        trip.tripPrefix,
                        trip.tripCode,
                        input.tripDestinationSeq
                    )
                    //
                    coordinatesDestination?.let {
                        calculateDistance(it, coordinates)
                    }
                    //
                } ?: calculateDistance(
                    it.originCoordinates,
                    coordinates
                )
            }
        }

        if (savedDate == null) {
            trip?.let {

                var isRef = 1
                val positionCounter = 0
                val transmissionCounter = 0
                //
                val tripPrefModel = currentTripPref.read()

                currentTripPref.write(
                    tripPrefModel.copy(
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
                        transmission_counter = transmissionCounter,
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
                    isRef = 1
                )
            )
            return
        }

        var alertTypeAux = TripPositionAlertType.NULL
        distance?.let {
            if (checkAlertType(alertType, distance, positionDistanceMin)) {
                alertTypeAux = alertType
            }
        }

        trip?.let {
            val lastPosition = positionRepository.getLocationRefSave()
            val differenceMinutes =
                calculateMinutesBetweenDates(lastPosition?.date ?: date, date, DATE_FORMAT_DEFAULT)
            val currentDistance = calculateDistance(lastPosition ?: input.coordinates, coordinates)
            val tripPrefModel = currentTripPref.read()
            var positionCounter = tripPrefModel.position_counter + 1
            val transmissionCounter = tripPrefModel.transmission_counter + 1
            var isRef = if (it.tripStatus == TripStatus.ON_SITE.toDescription()) {
                checkOnSiteRef(positionCounter, it, differenceMinutes, currentDistance, trip)
            } else {
                if (positionCounter >= it.distanceRefMinutes) {
                    1
                } else {
                    0
                }
            }
            //
            Log.d("TRIP_SERVICE", "------TripPositionAlertType-------")
            Log.d("TRIP_SERVICE", "alertTypeAux: $alertTypeAux")
            Log.d("TRIP_SERVICE", "distance: $distance")
            //
            if (alertTypeAux !=  TripPositionAlertType.NULL
                && prevAlertType != alertTypeAux) {
                isRef = 1
                positionRepository.changeStatusNotification(alertTypeAux)
                tripPrefModel.last_alert_type = alertTypeAux.toDescription()
            }else{
                if (alertTypeAux == TripPositionAlertType.NULL){
                    positionRepository.changeStatusNotification(alertTypeAux)
                }
            }
            //
            if (isRef == 1) {
                tripPrefModel.isRef = 1
                tripPrefModel.ref_latitude = coordinates.latitude
                tripPrefModel.ref_longitude = coordinates.longitude
                tripPrefModel.ref_location_date = coordinates.date
                positionCounter = 0
            }
            //
            currentTripPref.write(
                tripPrefModel.copy(
                    position_counter = positionCounter,
                    transmission_counter = transmissionCounter,
                    customer_code = trip.customerCode,
                    trip_prefix = trip.tripPrefix,
                    trip_code = trip.tripCode,
                    trip_scn = trip.scn,
                    position_seq = -1,
                    destination_seq = input.tripDestinationSeq,
                )
            )
            //
            positionSaveUseCase(
                PositionSaveUseCase.Params(
                    coordinates = coordinates,
                    alertType = if (alertTypeAux == TripPositionAlertType.NULL) null else alertTypeAux,
                    destinationSeq = input.tripDestinationSeq,
                    isRef
                )
            )
            //
            if (transmissionCounter >= trip.distanceRefMinutesTrans
                && tripPrefModel.isRef == 1
            ) {
                positionRepository.execPositionSave()
            }
            //
        }
    }

    private fun checkOnSiteRef(
        positionCounter: Int,
        it: FSTrip,
        differenceMinutes: Long,
        currentDistance: Double,
        trip: FSTrip
    ) = if (positionCounter >= it.distanceRefMinutes
        && (differenceMinutes >= 60 || currentDistance >= (trip.positionDistanceMin ?: 0.5))
    ) {
        1
    } else {
        0
    }

    private fun checkAlertType(
        alertType: TripPositionAlertType,
        distance: Double,
        positionDistanceMin: Double
    ) = (alertType == TripPositionAlertType.DEPARTED && distance >= positionDistanceMin
            ||
            alertType == TripPositionAlertType.ARRIVED && distance <= positionDistanceMin)

}
