package com.namoadigital.prj001.ui.act083.model

import com.namoadigital.prj001.model.SerialSiteInventory


sealed class TypeSerial {

    object MY_ACTIONS : TypeSerial()
    data class MORE_ACTIONS(val model: SerialSiteInventory) : TypeSerial()
    data class INFO_SERIAL(val model: SerialSiteInventory) : TypeSerial()
    object NULL : TypeSerial()

    companion object {
        const val SERIAL_SITE_ACTION_BASE = "SERIAL_SITE_ACTION_BASE"
    }
}
