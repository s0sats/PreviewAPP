package com.namoadigital.prj001.ui.act005.trip.di.usecase.event

import com.namoadigital.prj001.core.trip.domain.usecase.GetEventRestrictionDateUseCase

data class TripEventUseCase(
    val getEventType: GetEventTypeUseCase,
    val listEventTypes: GetListEventTypeUseCase,
    val save: SaveEventUseCase,
    val getEventRestrictionDate: GetEventRestrictionDateUseCase,
)