package com.namoadigital.prj001.ui.act095.event_manual.domain.repository

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import kotlinx.coroutines.flow.Flow

interface EventManualRepository {

    fun hasAccessToEventManual(): Boolean

    suspend fun getListEventType(): Flow<IResult<List<FSEventType>>>

    fun geEventTypeByCode(code: Int): FSEventType?

    suspend fun saveEvent(eventManualData: EventManualData): Flow<IResult<EventManual>>

    suspend fun sendEvents(): Flow<IResult<Unit>>

    suspend fun getListEvents(date: Int): Flow<IResult<List<EventManual>>>

    suspend fun getEventByDate(date: Int): Flow<IResult<List<EventManual>>>

    fun getEventActive(): EventManual?

    fun getEventPendency(): List<EventManual>

    fun getEventConflict(
        currentSeq: Int? = null,
        eventDay: Int? = null,
        startDate: String,
        endDate: String?
    ): EventConflict?

    fun getHistoryEventsByDay(eventDay: Int): Flow<IResult<List<EventManual>>>


}