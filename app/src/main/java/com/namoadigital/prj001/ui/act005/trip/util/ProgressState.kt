package com.namoadigital.prj001.ui.act005.trip.util

sealed class ProgressState {

    data class Online(
        val process: String,
        val title: String,
        val message: String
    ) : ProgressState()

    object Offline : ProgressState()

    data class Hide(val onlyClose: Boolean = false) : ProgressState()

    data class Error(
        val throwable: Throwable,
        val closeDialog: Boolean = false,
        val errorMsg: String? = null,
    ) : ProgressState()

}
