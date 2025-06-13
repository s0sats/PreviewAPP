package com.namoadigital.prj001.ui.act011.group_verification.domain.model

import com.namoadigital.prj001.model.masterdata.ge_os.ProcessVg
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.ui.act011.group_verification.VerificationGroupFragment.Companion.LOADING_LBL

data class VerificationGroupState(
    val stateLoading: StateLoading = StateLoading(),
    val listGroups: List<VerificationGroup> = emptyList(),
    val listGeOsVgs: List<GeOsVg> = emptyList(),
    val error: String? = null,
    val formPK: FormPK = FormPK(),
    val hasProcessVg: ProcessVg? = null,
    val updateScreens: Boolean = false,
    val isReadOnly: Boolean = false,
){

    val disableLoading = StateLoading(false)
    val enableLoading = StateLoading()

    data class StateLoading(
        val isLoading: Boolean = true,
        val message: String = LOADING_LBL,
    )

    data class FormPK(
        val customerCode: Long = -1L,
        val customFormType: Int = 1,
        val customFormCode: Int = -1,
        val customFormVersion: Int = -1,
        val customFormData: Int = -1,
        val productCode: Long = -1L,
        val serialCode: Long = -1L,
    )

}