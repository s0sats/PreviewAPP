package com.namoadigital.prj001.ui.act083.model

import com.namoadigital.prj001.model.SerialSiteInventory


sealed class TypeSerial {

    object MY_ACTIONS : TypeSerial()
    object SERIAL_SITE : TypeSerial()
    data class INFO_SERIAL(val model: SerialSiteInventory) : TypeSerial()
    object NULL : TypeSerial()

}
