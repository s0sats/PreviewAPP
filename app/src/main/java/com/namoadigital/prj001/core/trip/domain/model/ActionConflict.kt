package com.namoadigital.prj001.core.trip.domain.model

import android.content.Context
import com.namoadigital.prj001.extensions.date.formatRange

data class ActionConflict(
    val dateStart: String?,
    val dateEnd: String?,
    val type: ActionConflictType
) {

    fun range(context: Context): String = context.formatRange(dateStart, dateEnd)

}

enum class ActionConflictType {
    START_OVERLAP,
    END_OVERLAP,
    RANGE_OVERLAP
}