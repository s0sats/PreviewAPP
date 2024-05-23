package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.data.local.repository.siteInventory.SerialSiteInventoryRepository

class GetPreferenceSiteInvUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke(): SiteInventory {
        return repository.getPreference()
    }

}