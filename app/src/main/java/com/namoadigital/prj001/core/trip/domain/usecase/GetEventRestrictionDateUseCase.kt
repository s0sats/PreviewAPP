package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository

class GetEventRestrictionDateUseCase constructor(
    private val repository: TripEventRepository
): UseCaseWithoutFlow<GetEventRestrictionDateUseCase.InputParams, GetEventRestrictionDateUseCase.OutputParams> {

    override fun invoke(input: InputParams): OutputParams {
        return repository.checkRestrictionDate(input.startDateinMillis,input.endDateinMillis, input.event, input.waiting)
    }
    data class InputParams(
        val startDateinMillis: Long,
        val endDateinMillis: Long?,
        val event: FSTripEvent?,
        val waiting: Boolean,
    )

    data class OutputParams(
        val event: FSTripEvent?,
        val startDateError: Boolean,
        val endDateError: Boolean,
    )

}
