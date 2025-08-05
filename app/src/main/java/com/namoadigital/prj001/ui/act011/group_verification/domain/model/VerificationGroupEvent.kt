package com.namoadigital.prj001.ui.act011.group_verification.domain.model

import com.namoadigital.prj001.model.masterdata.ge_os.ProcessVg

sealed class VerificationGroupEvent {
    data class onGroupSwitchChange(
        val isContinousForm: Boolean,
        val vgCode: Int,
        val isChecked: Boolean
    ) : VerificationGroupEvent()
    data class OnHandleListVerificationGroup(
        val hasProcessVg: ProcessVg? = null,
        val formPKs: VerificationGroupState.FormPK,
        val isReadOnly: Boolean,
        val processType: String
    ) : VerificationGroupEvent()
    data object OnRetry : VerificationGroupEvent()
    data class OnUpdateScreens(val isFailed: Boolean?) : VerificationGroupEvent()
}