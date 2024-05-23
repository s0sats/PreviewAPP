package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import com.namoadigital.prj001.core.data.local.repository.siteInventory.SerialSiteInventoryRepository

class CheckSiteInventoryUseCase constructor(
    private val repository: SerialSiteInventoryRepository
) {

    operator fun invoke(type: CheckType): Boolean {
        return when (type) {
            CheckType.REFRESH -> repository.getPreference().refresh
            CheckType.FILE_EXIST -> repository.getPreference().site_code != -1
        }
    }

}
enum class CheckType {
    REFRESH,
    FILE_EXIST
}