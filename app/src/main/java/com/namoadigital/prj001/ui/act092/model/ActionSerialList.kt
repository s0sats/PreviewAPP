package com.namoadigital.prj001.ui.act092.model

import androidx.annotation.DrawableRes
import com.namoadigital.prj001.model.action_serial.ActionsCache

data class ActionSerialList(
    val actionsCache: ActionsCache,
    val processStatusTrans: String? = null,
    @DrawableRes val processLeftIcon: Int? = null,
    @DrawableRes val processRightIcon: Int? = null,
    val highlightItem: Boolean = false,
    val periodStarted: Boolean = false,
    val lateItem: Boolean = false,
    val isLastSelectedItem: Boolean = false,
) {

    val getSiteZoneDesc = actionsCache.zoneDesc?.let {
        if (it.isNotEmpty()) "${actionsCache.siteDesc} | ${actionsCache.zoneDesc}"
        else actionsCache.siteDesc
    } ?: actionsCache.siteDesc

    val allFieldForFilter = "${actionsCache.processId}|" +
            "$processStatusTrans|" +
            "${actionsCache.plannedDate}|" +
            "${actionsCache.tagOperationDesc}|" +
            "${actionsCache.processDesc}|" +
            "${actionsCache.originDescriptor}|" +
            "${actionsCache.processDesc}|" +
            "${actionsCache.internalComments}|" +
            "${actionsCache.focusStepDesc}|" +
            "${actionsCache.siteDesc}|" +
            "${actionsCache.zoneDesc}"
                .replace("null|", "")
                .replace("null", "")

}