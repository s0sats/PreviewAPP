package com.namoadigital.prj001.ui.act095.event_manual.presentation.balloon

import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Responsável por manter e observar o estado do EventManual.
 */
class EventManualStateHolder(
    private val getEventManualUseCase: GetEventManualUseCase,
    private val scope: CoroutineScope,
    private val onEventChanged: ((EventManualBalloonState) -> Unit)? = null
) {
    private val _state = MutableStateFlow(EventManualBalloonState())
    val state: StateFlow<EventManualBalloonState> = _state.asStateFlow()

    private var timeUpdaterJob: Job? = null

    init {
        observeStateChanges()
        loadEventManual()
    }

    private fun observeStateChanges() {
        state.onEach { currentState ->
            onEventChanged?.invoke(currentState)
        }.launchIn(scope)
    }

    private fun startTimeUpdater() {
        timeUpdaterJob?.cancel()
        timeUpdaterJob = scope.launch(Dispatchers.Main) {
            while (isActive && getCurrentState().hasActiveEvent()) {
                delay(1_500L)
                onEventChanged?.invoke(_state.value)
            }
        }
        timeUpdaterJob?.start()
    }

    fun loadEventManual() {
        scope.launch(Dispatchers.IO) {
            _state.value = _state.value.withLoading(true)
            try {
                val event = getEventManualUseCase(Unit)
                _state.value = _state.value.withSuccess(event)
                startTimeUpdater()
            } catch (e: Exception) {
                _state.value = _state.value.withError(e.message)
            }
        }
    }

    fun refresh() {
        loadEventManual()
    }

    fun getCurrentState(): EventManualBalloonState = _state.value
}

