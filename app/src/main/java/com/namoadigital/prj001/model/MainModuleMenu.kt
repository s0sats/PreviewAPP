package com.namoadigital.prj001.model

import androidx.annotation.DrawableRes
import com.namoadigital.prj001.util.ConstantBaseApp

class MainModuleMenu(val moduleId: Int, val moduleIcon: Int, val moduleTitle: String, val moduleDetail: String, private val updateRequired: Int, private val syncRequired: Int) {
    var status: String? = null
    //
    init {
        status = if (updateRequired == 1 && syncRequired == 1) {
            ConstantBaseApp.MAIN_TAG_MENU_SYNC_DATA
        } else if (updateRequired == 1) {
            ConstantBaseApp.MAIN_TAG_MENU_SEND_DATA
        } else if (syncRequired == 1) {
            ConstantBaseApp.MAIN_TAG_MENU_RECEIVE_DATA
        } else {
            ConstantBaseApp.MAIN_TAG_MENU_DATA_OK
        }
    }

    companion object{
        const val ID_MODULE_OS_NEXT = 1
        const val ID_MODULE_OS_VIN_SEARCH = 2
        const val ID_MODULE_OS_EXPRESS = 3
        const val ID_MODULE_OS = 4
        const val ID_MODULE_ASSETS = 5
        const val ID_MODULE_TAGS = 6
        const val ID_MODULE_TAGS_BY_SERIAL_SEARCH = 7
    }
}