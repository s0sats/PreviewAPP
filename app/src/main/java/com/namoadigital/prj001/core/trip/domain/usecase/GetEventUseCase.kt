package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.model.trip.FSTripEvent

class GetEventUseCase constructor(
    private val repository: TripRepository
): UseCaseWithoutFlow<Unit, FSTripEvent?>{
    override fun invoke(input: Unit): FSTripEvent? {
       return repository.getEvent()
    }

}
