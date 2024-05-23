package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import android.os.Bundle
import com.namoadigital.prj001.core.data.local.repository.siteInventory.SerialSiteInventoryRepository
import com.namoadigital.prj001.service.WsSerialSiteInventory

class ExecServiceSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke() {
        Bundle().apply {
            putInt(WsSerialSiteInventory.SITE_CODE, repository.getPreference().site_code)
            repository.execServiceSiteInventory(this)
        }

    }

}