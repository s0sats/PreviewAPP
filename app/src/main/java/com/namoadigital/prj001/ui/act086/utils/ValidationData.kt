package com.namoadigital.prj001.ui.act086.utils

data class ConfirmationInfo(
    val shouldShow: Boolean,
    val title: String? = null,
    val message: String? = null,
    val reason: DialogReason? = null
)

enum class DialogReason {
    MEASUREMENT_OR_MATERIAL_CHANGE,
    MATERIAL_ONLY_CHANGE
}