package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import javax.inject.Inject

class GetCountPendencyEventUseCase @Inject constructor(
    private val repository: EventManualRepository
) : UseCaseWithoutFlow<Unit, List<EventManual>> {

    override fun invoke(input: Unit): List<EventManual> {
        return repository.getEventPendency()
    }

}
