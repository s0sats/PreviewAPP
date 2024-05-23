package com.namoadigital.prj001.ui.act005.trip.di.usecase.event

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository

class GetEventTypeUseCase constructor(
    private val repository: TripEventRepository
) : UseCaseWithoutFlow<GetEventTypeUseCase.InputParams, FSEventType?> {


    data class InputParams(
        val customerCode: Long,
        val eventTypeCode: Int,
    )
    override fun invoke(input: InputParams): FSEventType? {
        return repository.getEventType(input.customerCode, input.eventTypeCode)
    }

}
