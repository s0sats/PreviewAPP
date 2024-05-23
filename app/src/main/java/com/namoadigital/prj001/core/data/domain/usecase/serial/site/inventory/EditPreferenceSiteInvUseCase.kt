package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.local.repository.siteInventory.SerialSiteInventoryRepository

class EditPreferenceSiteInvUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke(map: HashMap<String, Any>) {
        repository.editPreference(map)
    }

}
