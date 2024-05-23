package com.namoadigital.prj001.ui.act005.trip.di.usecase.event

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event.FSSaveEvent
import com.namoadigital.prj001.ui.act005.trip.repository.event.TripEventRepository

class SaveEventUseCase constructor(
    private val repository: TripEventRepository
) : UseCaseWithoutFlow<FSSaveEvent, Unit> {
    override fun invoke(input: FSSaveEvent) {
        repository.saveEvent(
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
