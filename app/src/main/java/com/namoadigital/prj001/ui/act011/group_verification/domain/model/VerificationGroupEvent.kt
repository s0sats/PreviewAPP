package com.namoadigital.prj001.ui.act011.group_verification.domain.model

sealed class VerificationGroupEvent {
    data class onGroupSwitchChange(
        val isContinousForm: Boolean,
        val vgCode: Int,
        val isChecked: Boolean
    ) : VerificationGroupEvent()
    data class OnHandleListVerificationGroup(
        val hasForcedExpiredVg: Boolean = false,
        val formPKs: VerificationGroupState.FormPK,
        val isReadOnly: Boolean
    ) : VerificationGroupEvent()
    data object OnRetry : VerificationGroupEvent()
    data class OnUpdateScreens(val isFailed: Boolean?) : VerificationGroupEvent()
}