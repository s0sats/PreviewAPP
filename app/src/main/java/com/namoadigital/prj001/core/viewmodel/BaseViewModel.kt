package com.namoadigital.prj001.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.TranslateMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : TranslateState, Event>(
    initialState: State,
    protected val translateBuild: TranslateBuild,
    private val applyTranslation: (State, TranslateMap) -> State,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val translation = loadTranslation()
            updateState { applyTranslation(it, translation) }
        }
    }

    protected val _uiState = MutableStateFlow(initialState)
    val state = _uiState.asStateFlow()

    abstract fun onEvent(event: Event)

    protected fun updateState(updateBlock: (state: State) -> State) {
        _uiState.update(updateBlock)
    }


    protected open suspend fun loadTranslation(): TranslateMap {
        return translateBuild.build()
    }
}