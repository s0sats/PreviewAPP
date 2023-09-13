package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.local.repository.SerialSiteInventoryRepository

class DeleteSerialSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke() {
        repository.cleanPreference()
        repository.deleteFile()
    }

}