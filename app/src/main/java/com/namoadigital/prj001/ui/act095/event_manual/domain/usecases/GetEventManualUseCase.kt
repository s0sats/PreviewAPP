package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import javax.inject.Inject

class GetEventManualUseCase @Inject constructor(
    private val repository: EventManualRepository,
) : UseCaseWithoutFlow<Unit, EventManual?> {
    override fun invoke(input: Unit): EventManual? {
        val event = repository.getEventActive()

        if (event == null) return null

        val eventType = repository.geEventTypeByCode(event.typeCode)

        if (eventType == null) return null

        return event.apply {
            this.eventFieldConfig =
                EventManualData.EventFieldConfig().apply { setFieldConfig(eventType) }
        }

    }
}