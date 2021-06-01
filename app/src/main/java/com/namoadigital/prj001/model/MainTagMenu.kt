package com.namoadigital.prj001.model

import com.namoadigital.prj001.util.ConstantBaseApp.*

class MainTagMenu(val tagCode: Int,var tagName: String?, val tagQty: Int, val tagHasFormInExecution: Int, private val updateRequired: Int, private val syncRequired: Int) {
    var status: String? = null

    init {
        status = if (updateRequired == 1 && syncRequired == 1) {
            MAIN_TAG_MENU_SYNC_DATA
        } else if (updateRequired == 1) {
            MAIN_TAG_MENU_SEND_DATA
        } else if (syncRequired == 1) {
            MAIN_TAG_MENU_RECEIVE_DATA
        } else {
            MAIN_TAG_MENU_DATA_OK
        }
    }
}