package com.namoadigital.prj001.ui.act005.trip.repository.event

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import kotlinx.coroutines.flow.Flow

interface TripEventRepository {

    fun getListEventType() : List<FSEventType>
    fun getEventType(customerCode: Long, eventTypeCode: Int): FSEventType?

    fun saveEvent(
        type: FSEventType,
        cost: Double?,
        comment: String?,
        photoPath: String?,
        changePhoto: Int,
        seq: Int?,
        eventStatus: EventStatus,
        dateStart: String?,
        dateEnd: String?
    ): Flow<IResult<Unit>>

    fun getExtract(trip: FSTrip?) : List<Extract<FSTripEvent>>

    fun checkRestrictionDate(
        startDateInMilis: Long,
        endDateInMilis: Long?,
        event: FSTripEvent?,
        waiting: Boolean
    ): GetEventRestrictionDateUseCase.OutputParams
    fun getFirstEventOnTrip(customerCode: Long, tripPrefix: Int, tripCode: Int): FSTripEvent?
}