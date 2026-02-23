package com.namoadigital.prj001.ui.act005.trip.di.usecase.event

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.FSSaveEvent
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository
import kotlinx.coroutines.flow.Flow

class SaveEventUseCase constructor(
    private val repository: TripEventRepository
) : UseCases<FSSaveEvent, Unit> {
    override suspend fun invoke(input: FSSaveEvent) : Flow<IResult<Unit>> {
        return repository.saveEvent(
            input.type,
            input.cost,
            input.comment,
            input.photoPath,
            if(input.changePhoto == true) 1 else 0,
            input.seq,
            input.eventStatus,
            input.dateStart,
            input.dateEnd
        )
    }

}
