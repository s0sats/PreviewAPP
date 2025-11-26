package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryEventsUseCase @Inject constructor(
    private val repository: EventManualRepository
) : UseCases<Int, List<EventManual>> {

    override suspend fun invoke(input: Int): Flow<IResult<List<EventManual>>> {
        return repository.getHistoryEventsByDay(input)
    }

}
