package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import javax.inject.Inject

class GetEventActiveUseCase @Inject constructor(
    private val repository: TripEventRepository
) : UseCaseWithoutFlow<Unit, FSTripEvent?> {
    override fun invoke(input: Unit): FSTripEvent? {
        return repository.getEventActive()
    }
}