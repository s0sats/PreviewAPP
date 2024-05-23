package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util

import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginTypeSave

sealed class SaveOriginEdit() {

    data class ALL(
        val date: String,
        val fleet: String,
        val odometer: String,
        val photoUpdate: PhotoUpdate
    ) : SaveOriginEdit()

    data class FLEET(
        val fleet: String,
        val odometer: String,
        val photoUpdate: PhotoUpdate
    ) : SaveOriginEdit()

    data class ORIGIN(
        val date: String
    ) : SaveOriginEdit()

    object NOTHING : SaveOriginEdit()

}