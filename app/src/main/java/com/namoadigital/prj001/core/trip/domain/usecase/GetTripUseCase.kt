package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.FSTrip

class GetTripUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<Unit, FSTrip?> {
    override fun invoke(input: Unit): FSTrip? {
        return repository.getTrip()
    }

}