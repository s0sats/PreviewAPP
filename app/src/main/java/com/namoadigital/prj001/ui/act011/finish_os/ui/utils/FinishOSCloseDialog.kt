package com.namoadigital.prj001.ui.act011.finish_os.ui.utils

import com.namoa_digital.namoa_library.ctls.MKEditTextNM


interface FinishOSCloseDialog {
    fun action()
    fun setBackupMachineSerialRecovery(mkEditTextNM: MKEditTextNM)
    fun setWsProcess(wsSoProcess: String?)
}