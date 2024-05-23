package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv

class TripStatusChangeUseCase constructor(
    private val repository: TripRepository
) : UseCaseWithoutFlow<TripStatusChangeEnv, Unit> {
    override fun invoke(
        request: TripStatusChangeEnv
    ) {
        return repository.setTripStatus(request)
    }

}