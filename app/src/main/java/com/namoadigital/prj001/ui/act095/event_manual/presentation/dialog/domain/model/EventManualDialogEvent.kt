package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model

import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.ui.EventUiState

sealed class EventManualDialogEvent {

    data class SaveDialogEvent(
        val data: EventManualData,
        val isEditMode: Boolean
    ) : EventManualDialogEvent()

    class UpdateDialogEventData(val data: EventManualData) : EventManualDialogEvent()
    class UpdateDialogEventState(val uiState: EventUiState) : EventManualDialogEvent()
    class ValidateDate(val startDate: String, val endDate: String?, val withWaiting: Boolean = false) : EventManualDialogEvent()

    object GetListDialogEventType : EventManualDialogEvent()
}