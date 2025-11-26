package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model

import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.viewmodel.TranslateState
import com.namoadigital.prj001.model.trip.FSEventType
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui.EventUiState
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey

data class EventManualDialogState(
    val screensLoading: Boolean = false,
    val isLoading: Boolean = false,
    val eventUiState: EventUiState = EventUiState.Loading,
    val listEventType: List<FSEventType> = emptyList(),
    override val translate: TranslateMap = emptyMap(),
    var showProgress: Boolean = false,
    val eventData: EventManualData? = null,
    val errorMessage: String? = null,
    val startDateError: ErrorDate? = null,
    val endDateError: ErrorDate? = null
) : TranslateState {

    data class ErrorDate(
        val key: EventManualKey,
        val date: String = ""
    )

}