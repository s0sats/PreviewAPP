package com.namoadigital.prj001.ui.act093.util

import android.content.DialogInterface

sealed class Act093Event {

    object OnUpdateScreen : Act093Event()
    object OnMeasureNotFound : Act093Event()
    object OnUpdateList : Act093Event()
    object OnLoading : Act093Event()

    data class Toast(val message: String) : Act093Event()

    data class OpenDialog(val dialogType: DialogType) : Act093Event() {

        sealed class DialogType {
            data class PROCESS(val title: String?, val message: String?) : DialogType()

            data class ACTION(
                val title: String?,
                val message: String?,
                val action: DialogInterface.OnClickListener,
                val negativeBtn: Int = 0
            ) : DialogType()

            data class DEFAULT_OK(val title: String?, val message: String?) : DialogType()
            data class CUSTOM_OK(val title: String?, val message: String?) : DialogType()
        }
    }

}
