package com.namoadigital.prj001.ui.act095.event_manual.util

import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualData
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventManualDialogState


enum class ValidateEventManualField {
    COST, COMMENT, PHOTO, DATE
}

fun validateEventManualFields(
    data: EventManualData,
    endDateError: EventManualDialogState.ErrorDate?,
    hasPhoto: Boolean
): MutableMap<ValidateEventManualField, Boolean> {
    val config = data.eventFieldConfig

    return mutableMapOf(
        ValidateEventManualField.COST to (config.isCostRequired() && data.cost.isNullOrEmpty()),
        ValidateEventManualField.COMMENT to (config.isCommentRequired() && data.comment.isNullOrBlank()),
        ValidateEventManualField.PHOTO to (config.isPhotoRequired() && !hasPhoto),
        ValidateEventManualField.DATE to (endDateError != null)
    )
}
