package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.util

import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType

sealed class OriginOption(val originType: OriginType) {
    data class SITE(
        val code: Int? = null,
        val desc: String? = null
    ) : OriginOption(OriginType.SITE)
    object GPS : OriginOption(OriginType.GPS)
}