package com.namoadigital.prj001.ui.act087

import com.namoadigital.prj001.core.trip.domain.model.blockchain.ValidationResult
import com.namoadigital.prj001.model.BaseSerialSearchItem

interface FormOsHeaderFrgInfr {
    fun reportSerialBkpMachineToFrag(serialBkpMachineList: List<BaseSerialSearchItem>, onlineSearch: Boolean)
    fun isAnyDataChanged() : Boolean
    fun updateByValidateResult(action: ValidationResult)
}