package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.data.remote.sync.ISyncRepository.Companion.SyncRepositoryFactory
import com.namoadigital.prj001.ui.act092.data.repository.IActionSerialRepository.Companion.ActionSerialRepositoryFactoryRepository
import com.namoadigital.prj001.ui.base.NamoaFactory

data class ActionUseCases(
    val localTicket: ListMyActionUseCases,
    val syncFiles: SyncFilesUseCase,
    val syncFilesForm: SyncFilesFormUseCase,
    val downloadTicket: TicketDownloadUseCase,
    val checkSyncChecklist: CheckSyncUseCase,
    val validateNewForm: ValidateNewFormUseCase,
    val updateSyncCheckist: UpdateSyncCheckListUseCase,
    val unfocusHistoricalAction: UnfocusHistoricalActionUseCases,
    val setPreferences: SetModelPreferencesUseCase,
    val getPreferences: GetSerialModelPreferencesUseCase
) {

    companion object {

        class ActionUseCasesFactory constructor(
            private val context: Context
        ) : NamoaFactory<ActionUseCases>() {
            override fun build(): ActionUseCases {

                val repository = ActionSerialRepositoryFactoryRepository(context).build()
                val syncRepository = SyncRepositoryFactory(context).build()

                return ActionUseCases(
                    localTicket = ListMyActionUseCases(context, repository),
                    syncFiles = SyncFilesUseCase(context, syncRepository),
                    unfocusHistoricalAction = UnfocusHistoricalActionUseCases(repository),
                    syncFilesForm = SyncFilesFormUseCase(syncRepository),
                    downloadTicket = TicketDownloadUseCase(context, repository),
                    checkSyncChecklist = CheckSyncUseCase(syncRepository),
                    validateNewForm = ValidateNewFormUseCase(context, repository),
                    updateSyncCheckist = UpdateSyncCheckListUseCase(context, repository),
                    setPreferences = SetModelPreferencesUseCase(repository),
                    getPreferences = GetSerialModelPreferencesUseCase(repository)
                )
            }
        }
    }

}
