package com.namoadigital.prj001.ui.act093.ui.util

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class ShowDialog(val title: String, val message: String) : UiEvent()
}