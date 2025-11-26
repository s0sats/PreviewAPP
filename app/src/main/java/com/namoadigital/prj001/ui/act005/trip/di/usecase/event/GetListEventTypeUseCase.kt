package com.namoadigital.prj001.ui.act005.trip.di.usecase.event

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import javax.inject.Inject

class GetListEventTypeUseCase @Inject constructor(
    private val repository: TripEventRepository
) : UseCaseWithoutFlow<Unit, List<FSEventType>>{
    override fun invoke(input: Unit): List<FSEventType> {
        return repository.getListEventType()
    }

}
