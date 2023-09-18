package com.namoadigital.prj001.core.data.domain.usecase.serial.site.inventory

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.SerialSiteInventoryRepositoryImp

data class SerialSiteInventoryUseCase(
    val getSiteInventory: GetSiteInventoryUseCase? = null,
    val service: ExecServiceSiteInventoryUseCase? = null,
    val savePreference: SavePreferenceSiteInvUseCase? = null,
    val editPreference: EditPreferenceSiteInvUseCase? = null,
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

            fun getAndcheckAndExecUseCase(): SerialSiteInventoryUseCase {
                return SerialSiteInventoryUseCase(
                    check = CheckSiteInventoryUseCase(repository),
                    service = ExecServiceSiteInventoryUseCase(repository),
                    getPreference = GetPreferenceSiteInvUseCase(repository),
                    getSiteInventory = GetSiteInventoryUseCase(repository)
                )
            }

            fun editPrefrenceFileUseCase(): SerialSiteInventoryUseCase {
                return SerialSiteInventoryUseCase(
                    editPreference = EditPreferenceSiteInvUseCase(repository)
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