package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv

class SetDestinationStatusUseCase constructor(
    private val repository: TripDestinationRepository
) : UseCaseWithoutFlow<TripDestinationStatusChangeEnv, Unit> {
    override fun invoke(input: TripDestinationStatusChangeEnv) {
        repository.setTripDestinationStatusChange(input)
    }

}