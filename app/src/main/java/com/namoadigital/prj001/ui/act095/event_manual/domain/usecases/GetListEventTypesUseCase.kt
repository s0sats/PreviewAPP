package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act095.event_manual.domain.repository.EventManualRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListEventTypesUseCase @Inject constructor(
    private val repository: EventManualRepository,
) : UseCases<Unit, List<FSEventType>> {
    override suspend fun invoke(input: Unit): Flow<IResult<List<FSEventType>>> {
        return repository.getListEventType()
    }
}