package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory.SerialSiteInventoryUseCase

fun SerialSiteInventoryUseCase.updateSerialSiteInventoryRefresh(refresh: Boolean) {
    val editPref = HashMap<String, Any>()
    editPref["refresh"] = refresh
    this.editPreference?.invoke(editPref)
}