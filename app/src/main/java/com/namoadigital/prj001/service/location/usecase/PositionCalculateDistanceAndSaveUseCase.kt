package com.namoadigital.prj001.service.location.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.extensions.date.DATE_FORMAT_DEFAULT
import com.namoadigital.prj001.extensions.date.calculateMinutesBetweenDates
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.service.location.util.calculateDistance

class PositionCalculateDistanceAndSaveUseCase constructor(
    private val tripRepository: TripRepository,
    private val positionRepository: TripPositionRepository,
    private val positionSaveUseCase: PositionSaveUseCase,
    private val currentTripPref: CurrentTripPref,
    ): UseCaseWithoutFlow<PositionCalculateDistanceAndSaveUseCase.Params, Unit>{

    data class Params(
        val coordinates: Coordinates,
        val alertType: TripPositionAlertType?,
        val tripDestinationSeq: Int?
    )
    override fun invoke(input: Params) {

        val (coordinates, alertType) = input
        val date = coordinates.date!!
        val trip = tripRepository.getTrip()
        val lastPosition = positionRepository.getLocationRefSave()

        val differenceMinutes = lastPosition?.date?.let {
             calculateMinutesBetweenDates(it, date, DATE_FORMAT_DEFAULT)
        } ?: run {
            val newDate = getCurrentDateApi()
            input.coordinates.date = newDate
            calculateMinutesBetweenDates(getCurrentDateApi(), date, DATE_FORMAT_DEFAULT)
        }
        //
        val currentDistance = calculateDistance(lastPosition ?: input.coordinates, coordinates)
        val locationPref = positionRepository.getLocationPref()
        //
        trip?.let{
            locationPref?.let {
                //
                var isRef = 0
                it.position_counter += 1
                it.transmission_counter += 1
                //
                if((it.position_counter >= trip.distanceRefMinutes)
                    && (differenceMinutes >= 60
                        || currentDistance >= (trip.positionDistanceMin?:0.5)
                        )
                ) {
                    isRef = 1
                    it.isRef = 1
                    it.ref_latitude = coordinates.latitude
                    it.ref_longitude = coordinates.longitude
                    it.ref_location_date = coordinates.date
                    it.position_counter = 0
                }
                //
                currentTripPref.write(
                    it.copy(
                        customer_code = trip.customerCode,
                        trip_prefix = trip.tripPrefix,
                        trip_code = trip.tripCode,
                        trip_scn = trip.scn,
                        position_seq=-1,
                        last_alert_type = alertType?.toDescription(),
                        destination_seq = input.tripDestinationSeq,
                    )
                )
                //
                positionSaveUseCase(
                    PositionSaveUseCase.Params(
                        coordinates = coordinates,
                        alertType = alertType,
                        destinationSeq = input.tripDestinationSeq,
                        isRef = isRef
                    )
                )
                //
                if(it.transmission_counter >= trip.distanceRefMinutesTrans
                    && it.isRef == 1
                ){
                    positionRepository.execPositionSave()
                }
                //
            }
        }

    }
}