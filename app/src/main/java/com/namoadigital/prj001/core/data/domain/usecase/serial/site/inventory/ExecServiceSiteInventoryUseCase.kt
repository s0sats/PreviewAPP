package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import android.os.Bundle
import com.namoadigital.prj001.core.data.local.repository.SerialSiteInventoryRepository
import com.namoadigital.prj001.service.WsSerialSerialInventory
import com.namoadigital.prj001.util.ConstantBaseApp

class ExecServiceSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke() {
        Bundle().apply {
            putInt(WsSerialSerialInventory.SITE_CODE, repository.getPreference().site_code)
            putInt(ConstantBaseApp.GC_STATUS_JUMP, 1)
            putInt(ConstantBaseApp.GC_STATUS, 1)
            repository.execServiceSiteInventory(this)
        }

    }

}