package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.data.remote.sync.ISyncRepository.Companion.SyncRepositoryFactory
import com.namoadigital.prj001.ui.act092.data.repository.IActionSerialRepository.Companion.ActionSerialRepositoryFactoryRepository
import com.namoadigital.prj001.ui.base.NamoaFactory


data class ActionPreferenceUseCases(
    val setPreferences: SetModelPreferencesUseCase,
) {
    class ActionUseCasesPreferenceFactory constructor(
        private val context: Context
    ) : NamoaFactory<ActionPreferenceUseCases>() {
        override fun build(): ActionPreferenceUseCases {
            val repository = ActionSerialRepositoryFactoryRepository(context).build()

            return ActionPreferenceUseCases(
                setPreferences = SetModelPreferencesUseCase(repository)
            )
        }

    }

}

data class ActionUseCases(
    val listMyActionUseCases: ListMyActionUseCases,
    val syncFiles: SyncFilesUseCase,
    val syncFilesForm: SyncFilesFormUseCase,
    val downloadTicket: TicketDownloadUseCase,
    val checkSyncChecklist: CheckSyncUseCase,
    val validateNewForm: ValidateNewFormUseCase,
    val updateSyncCheckist: UpdateSyncCheckListUseCase,
    val unfocusHistoricalAction: UnfocusHistoricalActionUseCases,
    val setPreferences: SetModelPreferencesUseCase,
    val getPreferences: GetSerialModelPreferencesUseCase,
    val checkIfFileExists: CheckFileExistsUseCase,
    val flowScheduleFromMyAction: FlowScheduleFromMyActionUseCase,
    val getScheduleFromMyAction: GetScheduleFromMyActionUseCase,
    val ticketCtrl: GetScheduleCtrlIfExistsUseCase,
    val scheduleFormLocalExists: ScheduleFormLocalExistsUseCase,
    val createFormLocalForSchedule: CreateFormLocalForScheduleUseCase,
    val serialSearch: SerialSearchUseCase,
    val processLocalSearchForSerialAction: ProcessLocalSearchForSerialActionUseCase,
    val checkTicketFlowAndCreate: CheckTicketFlowAndCreateUseCase
) {

    companion object {


        class ActionUseCasesFactory constructor(
            private val context: Context
        ) : NamoaFactory<ActionUseCases>() {

            override fun build(): ActionUseCases {
                val repository = ActionSerialRepositoryFactoryRepository(context).build()
                val syncRepository = SyncRepositoryFactory(context).build()

                val scheduleFormLocalExistsUseCase = ScheduleFormLocalExistsUseCase(repository)
                val getScheduleFromMyActionUseCase = GetScheduleFromMyActionUseCase(repository)
                return ActionUseCases(
                    listMyActionUseCases = ListMyActionUseCases(context, repository),
                    syncFiles = SyncFilesUseCase(context, syncRepository),
                    unfocusHistoricalAction = UnfocusHistoricalActionUseCases(repository),
                    syncFilesForm = SyncFilesFormUseCase(syncRepository),
                    downloadTicket = TicketDownloadUseCase(context, repository),
                    checkSyncChecklist = CheckSyncUseCase(syncRepository),
                    validateNewForm = ValidateNewFormUseCase(context, repository),
                    updateSyncCheckist = UpdateSyncCheckListUseCase(context, repository),
                    setPreferences = SetModelPreferencesUseCase(repository),
                    getPreferences = GetSerialModelPreferencesUseCase(repository),
                    checkIfFileExists = CheckFileExistsUseCase(repository),
                    flowScheduleFromMyAction = FlowScheduleFromMyActionUseCase(
                        context,
                        repository,
                        getScheduleFromMyActionUseCase
                    ),
                    getScheduleFromMyAction = GetScheduleFromMyActionUseCase(repository),
                    ticketCtrl = GetScheduleCtrlIfExistsUseCase(repository),
                    scheduleFormLocalExists = scheduleFormLocalExistsUseCase,
                    createFormLocalForSchedule = CreateFormLocalForScheduleUseCase(repository),
                    serialSearch = SerialSearchUseCase(repository),
                    processLocalSearchForSerialAction = ProcessLocalSearchForSerialActionUseCase(
                        repository
                    ),
                    checkTicketFlowAndCreate = CheckTicketFlowAndCreateUseCase(
                        context,
                        repository,
                        getScheduleFromMyActionUseCase
                    )
                )
            }
        }
    }

}
