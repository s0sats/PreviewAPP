package com.namoadigital.prj001.ui.act095.event_manual.presentation.balloon

import com.namoadigital.prj001.model.event.local.EventManual

/**
 * Representa o estado atual do evento manual em tela (reativo)
 */
data class EventManualBalloonState(
    val currentEvent: EventManual? = null,
    val isLoading: Boolean = false,
) {

    fun hasActiveEvent(): Boolean = currentEvent != null

    fun withLoading(isLoading: Boolean): EventManualBalloonState =
        copy(isLoading = isLoading)

    fun withSuccess(event: EventManual?): EventManualBalloonState =
        copy(currentEvent = event, isLoading = false)

    fun withError(message: String?): EventManualBalloonState =
        copy(isLoading = false)

}
