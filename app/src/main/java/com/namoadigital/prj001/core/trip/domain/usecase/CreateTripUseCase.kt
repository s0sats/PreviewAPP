package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.location.Coordinates

class CreateTripUseCase constructor (
    private val repository: TripRepository
): UseCaseWithoutFlow<Coordinates?, Unit> {

    override fun invoke(input: Coordinates?) {
        return repository.execCreateTrip(input)
    }
}