package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic

import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.di.EventTranslate
import com.namoadigital.prj001.core.viewmodel.BaseViewModel
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.EventManualUseCases
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain.EventHistoricState
import com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain.EventHistoryEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventHistoryViewModel @Inject constructor(
    @EventTranslate translateBuild: TranslateBuild,
    private val useCases: EventManualUseCases,
) : BaseViewModel<EventHistoricState, EventHistoryEvent>(
    initialState = EventHistoricState(isLoading = true),
    translateBuild = translateBuild,
    applyTranslation = { state, translate -> state.copy(translate = translate) }
) {

    override fun onEvent(event: EventHistoryEvent) {
        when (event) {
            is EventHistoryEvent.GetListHistory -> getEvents(event.eventDay)
        }
    }

    private fun getEvents(eventDay: Int) {
        viewModelScope.launch {
            useCases.getHistoryEvents(eventDay).collect { collect ->
                collect.watchStatus(
                    success = { list ->
                        updateState { it.copy(listHistoric = list, isLoading = false) }
                    },
                    loading = { isLoading, message ->
                        updateState { it.copy(isLoading = isLoading) }
                    }
                )
            }
        }
    }

}