package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.SerialSiteInventoryRepositoryImp

data class SerialSiteInventoryUseCase(
    val siteInventory: GetSiteInventoryUseCase? = null,
    val service: ExecServiceSiteInventoryUseCase? = null,
    val savePreference: SavePreferenceSiteInvUseCase? = null,
    val getPreference: GetPreferenceSiteInvUseCase? = null,
    val check: CheckSiteInventoryUseCase? = null,
    val cleanPref: CleanPrefSiteInventoryUseCase? = null,
    val deleteFile: DeleteSerialSiteInventoryUseCase? = null
) {

    companion object {


        class SiteInventoryUseCaseFactory(
            context: Context
        ) {
            val repository =
                SerialSiteInventoryRepositoryImp.Companion.RepositoryFactory(context).build()

            fun checkAndExecUseCase(): SerialSiteInventoryUseCase {
                return SerialSiteInventoryUseCase(
                    check = CheckSiteInventoryUseCase(repository),
                    service = ExecServiceSiteInventoryUseCase(repository),
                    getPreference = GetPreferenceSiteInvUseCase(repository)
                )
            }

            fun savePreferenceAndDeleteFileUseCase(): SerialSiteInventoryUseCase {
                return SerialSiteInventoryUseCase(
                    savePreference = SavePreferenceSiteInvUseCase(repository),
                    deleteFile = DeleteSerialSiteInventoryUseCase(repository)
                )
            }

            fun deleteSerialSiteInvFile(): SerialSiteInventoryUseCase {
                return SerialSiteInventoryUseCase(
                    deleteFile = DeleteSerialSiteInventoryUseCase(repository)
                )
            }
        }


    }

}