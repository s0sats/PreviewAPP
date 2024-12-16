package com.namoadigital.prj001.ui.act011.finish_os.di.usecase

class FinishOSUseCase(
    val getFinishOsData: GetFinishOsDataUseCase,
    val saveOs: SaveFormOsUseCase,
    val backupMachineSearch: BackupMachineSearchUseCase,
    val backupMachineSave: BackupMachineSaveUseCase
)