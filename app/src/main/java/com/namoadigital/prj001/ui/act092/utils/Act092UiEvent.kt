package com.namoadigital.prj001.ui.act092.utils

import android.os.Bundle
import com.namoadigital.prj001.model.MyActionsBase

sealed class Act092UiEvent {

    data class ListingSerialSteels(val list: List<MyActionsBase>) : Act092UiEvent()


    data class IsLoading(val isLoading: Boolean, val message: String) : Act092UiEvent()

    data class EmptyOrError(val sizeList: Int) : Act092UiEvent()

    data class ShowSnackbar(val message: String) : Act092UiEvent()

    object FilterMainUser : Act092UiEvent()

    data class CallAct(val classe: Class<*>, val bundle: Bundle? = null) : Act092UiEvent()

    data class OpenDialog(val process: Boolean = false, val title: String?, val message: String?) :
        Act092UiEvent()

    object UpdateTitleActionSerial : Act092UiEvent()
}