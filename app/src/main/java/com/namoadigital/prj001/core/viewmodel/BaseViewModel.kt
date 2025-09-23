package com.namoadigital.prj001.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State, Event> constructor(
    initialState: State,
) : ViewModel() {

    protected val _uiState = MutableStateFlow(initialState)
    val state = _uiState.asStateFlow()

    abstract fun onEvent(event: Event)

    protected fun updateState(updateBlock: (state: State) -> State) {
        _uiState.update(updateBlock)
    }
}