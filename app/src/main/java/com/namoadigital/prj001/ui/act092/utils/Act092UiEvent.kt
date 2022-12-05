package com.namoadigital.prj001.ui.act092.utils

import android.content.DialogInterface
import android.os.Bundle
import com.namoadigital.prj001.model.MyActionsBase

sealed class Act092UiEvent {

    data class ListingSerialSteels(val list: List<MyActionsBase>) : Act092UiEvent()

    data class IsLoading(val isLoading: Boolean, val message: String) : Act092UiEvent()

    data class EmptyOrError(val sizeList: Int) : Act092UiEvent()

    data class ShowSnackbar(val message: String) : Act092UiEvent()

    object FilterMainUser : Act092UiEvent()

    data class CallAct(val classe: Class<*>, val bundle: Bundle? = null) : Act092UiEvent()
    data class CallActForResult(val classe: Class<*>, val bundle: Bundle? = null, val code: Int) :
        Act092UiEvent()

    data class OpenDialog(val dialogType: DialogType) : Act092UiEvent() {

        sealed class DialogType {
            data class PROCESS(val title: String?, val message: String?) : DialogType()

            data class ACTION(
                val title: String?,
                val message: String?,
                val action: DialogInterface.OnClickListener,
                val negativeBtn: Int = 0
            ) : DialogType()

            data class DEFAULT_OK(val title: String?, val message: String?) : DialogType()
        }
    }

    object UpdateTitleActionSerial : Act092UiEvent()

    data class CheckIfFileExists(val exists: Boolean) : Act092UiEvent()

    object UpdateFooterInfos : Act092UiEvent()
}