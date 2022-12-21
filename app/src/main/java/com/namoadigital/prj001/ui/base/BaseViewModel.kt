package com.namoadigital.prj001.ui.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.ui.act093.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<STATE, EVENT>(private val initState: STATE) : ViewModel() {

    var state by mutableStateOf(initState)

    @Suppress("MemberVisibilityCanBePrivate")
    protected val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    abstract fun onEvent(event: EVENT)

    suspend fun showSnackbar(message: String) = _eventFlow.emit(UiEvent.ShowSnackbar(message))
}