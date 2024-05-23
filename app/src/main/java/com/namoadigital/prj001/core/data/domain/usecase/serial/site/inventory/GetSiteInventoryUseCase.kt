package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.local.repository.siteInventory.SerialSiteInventoryRepository
import com.namoadigital.prj001.model.SerialSiteInventory

class GetSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke(): List<SerialSiteInventory> {
        return repository.getSiteInventory()
    }

}