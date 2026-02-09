package com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model

import android.content.Context
import com.namoadigital.prj001.extensions.date.formatRange

data class EventConflict(
    val dateStart: String?,
    val dateEnd: String?,
    val type: EventConflictType,
    val description: String? = null
) {

    fun range(context: Context): String = context.formatRange(dateStart, dateEnd)

}

enum class EventConflictType {
    OPEN_EVENT_EXISTS,
    START_OVERLAP,
    END_OVERLAP,
    RANGE_OVERLAP
}
