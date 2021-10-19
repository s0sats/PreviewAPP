package com.namoadigital.prj001.ui.act087

import com.namoadigital.prj001.model.FormOsHeaderFrgSerialBkpItemAbs

interface FormOsHeaderFrgInfr {
    fun reportSerialBkpMachineToFrag( serialBkpMachineList: List<FormOsHeaderFrgSerialBkpItemAbs>, onlineSearch: Boolean)
    fun isAnyDataChanged() : Boolean
}