package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import javax.inject.Inject

class AccessToEventManualUseCase @Inject constructor(
    private val repository: EventManualRepository
) : UseCaseWithoutFlow<Unit, Boolean> {
    override fun invoke(input: Unit): Boolean {
        return repository.hasAccessToEventManual()
    }
}