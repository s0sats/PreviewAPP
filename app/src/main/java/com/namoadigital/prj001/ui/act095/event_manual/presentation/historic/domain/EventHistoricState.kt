package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain

import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.core.viewmodel.TranslateState
import com.namoadigital.prj001.model.event.local.EventManual

data class EventHistoricState(
    val isLoading: Boolean,
    val listHistoric: List<EventManual> = emptyList(),
    override val translate: TranslateMap = emptyMap()
) : TranslateState