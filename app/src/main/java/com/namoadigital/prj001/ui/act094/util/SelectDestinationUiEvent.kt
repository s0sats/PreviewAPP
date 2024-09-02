package com.namoadigital.prj001.ui.act094.util

import android.content.DialogInterface
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable

sealed class SelectDestinationUiEvent {

    data class ListingDestinations(val list: List<SelectionDestinationAvailable>?) :
        SelectDestinationUiEvent()

    object Loading : SelectDestinationUiEvent()

    data class OpenDialog(val dialogType: DialogType) : SelectDestinationUiEvent() {

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