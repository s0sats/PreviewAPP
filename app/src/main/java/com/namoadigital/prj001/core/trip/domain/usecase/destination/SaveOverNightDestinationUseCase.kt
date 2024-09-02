package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.FsTripDestination
import kotlinx.coroutines.flow.Flow

class SaveOverNightDestinationUseCase(
    private val repository: TripDestinationRepository,
    private val tripRepository: TripRepository,
    private val selectDestinationUseCase: SelectDestinationUseCase,
    private val saveDestinationUseCase: SaveDestinationUseCase
) : UseCases<SaveOverNightDestinationUseCase.Input, Unit> {

    data class Input(
        val latitude: Double,
        val longitude: Double
    )

    override suspend fun invoke(input: Input): Flow<IResult<Unit>> {
        return repository.execServiceOvernightDestination(
                    tripRepository.getTrip(),
                    destinationType = FsTripDestination.OVER_NIGHT_DESTINATION_TYPE,
                    currentLat = input.latitude,
                    currentLon = input.longitude,
                )
    }
}