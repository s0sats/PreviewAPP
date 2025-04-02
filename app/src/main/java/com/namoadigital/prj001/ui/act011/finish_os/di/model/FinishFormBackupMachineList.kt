package com.namoadigital.prj001.ui.act011.finish_os.di.model

import com.namoadigital.prj001.model.BaseSerialSearchItem

class FinishFormBackupMachineList(
    var fromRemote: Boolean= true,
    var backupList: List<BaseSerialSearchItem.BackupMachineSerialItem>?,
)