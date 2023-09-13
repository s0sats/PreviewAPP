package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.local.repository.SerialSiteInventoryRepository

class CheckSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke(): Boolean {
        return repository.getPreference().site_code != -1
    }

}