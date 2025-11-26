package com.namoadigital.prj001.ui.act095.event_manual.domain.usecases

import javax.inject.Inject


data class EventManualUseCases @Inject constructor(
    val save: SaveEventManualUseCase,
    val listEventType: GetListEventTypesUseCase,
    val hasAccessToEventManual: AccessToEventManualUseCase,
    val getCountPendency: GetCountPendencyEventUseCase,
    val getHistoryEvents: GetHistoryEventsUseCase
)