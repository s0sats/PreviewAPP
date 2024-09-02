package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.google.gson.GsonBuilder
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable

class SaveDestinationUseCase constructor(
    private val repository: TripDestinationRepository,
    private val tripRepository: TripRepository,
    private val checkNextStatusWhenNewDestinationUseCase: CheckNextStatusWhenNewDestinationUseCase
) : UseCaseWithoutFlow<SaveDestinationUseCase.GetDestinationParams, Boolean> {
    data class GetDestinationParams(
        val customerCode: Long,
        val response: String?,
        val destination: SelectionDestinationAvailable,
    )

    override fun invoke(input: GetDestinationParams): Boolean {
        val gson = GsonBuilder().serializeNulls().create()

        val model: SelectDestinationRec? = if (input.response == null) {
            tripRepository.getTrip()?.let { trip ->

                val addDestination = checkNextStatusWhenNewDestinationUseCase(
                    CheckNextStatusWhenNewDestinationUseCase.Params(
                        tripPrefix = trip.tripPrefix,
                        tripCode = trip.tripCode,
                        destinationType = input.destination.destinationType
                    )
                )
                val latitude:Double?
                val longitude:Double?
                if (input.destination.destinationType == FsTripDestination.OVER_NIGHT_DESTINATION_TYPE) {
                    latitude = addDestination.arrivedLat
                    longitude = addDestination.arrivedLon
                }else{
                    latitude = input.destination.lat
                    longitude = input.destination.lon
                }
                SelectDestinationRec(
                    tripPrefix = trip.tripPrefix,
                    tripCode = trip.tripCode,
                    scn = trip.scn,
                    destinationSeq = addDestination.destinationSeq ?: 1,
                    destinationStatus = addDestination.destinationStatus?.toDescription()
                        ?: DestinationStatus.NULL.toDescription(),
                    tripStatus = addDestination.tripStatus?.toDescription()
                        ?: TripStatus.NULL.toDescription(),
                    lat = latitude,
                    lon = longitude,
                    arrivedDate = addDestination.date,
                    arrivedLat = addDestination.arrivedLat,
                    arrivedLon = addDestination.arrivedLon,
                    distanceMin = trip.positionDistanceMin ?: 0.1
                )

            }

        } else {
            gson.fromJson(input.response, SelectDestinationRec::class.java)
        }
        //
        return model?.let { model->
            if (input.destination.destinationType == FsTripDestination.OVER_NIGHT_DESTINATION_TYPE) {
                repository.saveOverNightDestination(
                    input.customerCode,
                    model
                )
            } else {
                repository.saveDestination(
                    input.customerCode,
                    remoteDestination = model,
                    destination = input.destination,
                    isOnlineFLow = input.response != null
                )
            }
        }?: false
    }

}