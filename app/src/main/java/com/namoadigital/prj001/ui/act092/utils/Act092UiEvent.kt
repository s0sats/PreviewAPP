package com.namoadigital.prj001.ui.act092.utils

import com.namoadigital.prj001.model.MyActionsBase

sealed class Act092UiEvent {

    data class ListingSerialSteels(val list: List<MyActionsBase>) : Act092UiEvent()


    data class IsLoading(val isLoading: Boolean, val message: String) : Act092UiEvent()

    data class EmptyOrError(val isError: Boolean = false) : Act092UiEvent()

    data class ShowSnackbar(val message: String) : Act092UiEvent()

    object FilterMainUser : Act092UiEvent()

}