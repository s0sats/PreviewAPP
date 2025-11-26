package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain

sealed class EventHistoryEvent {

    data class GetListHistory(val eventDay: Int) : EventHistoryEvent()

}