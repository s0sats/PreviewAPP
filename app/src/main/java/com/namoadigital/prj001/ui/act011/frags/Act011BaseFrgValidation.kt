package com.namoadigital.prj001.ui.act011.frags

import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus

interface Act011BaseFrgValidation {
    fun getTabErrorCount(): Int
    fun getTabCount(): Int
    fun getTabObj(skipFieldValidation: Boolean = false): Act011FormTab
    fun getTabStatus() : Act011FormTabStatus
    fun getTabName() : String
    fun applyAutoAnswer(): Int
}