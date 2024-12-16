package com.namoadigital.prj001.ui.act011.finish_os.ui.utils

import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.ui.act011.finish_os.di.model.FinishOsData

data class FinishState(
    override val translateMap: TranslateMap,
    val backupMachineWSProgress: Boolean = false,
    val isLoading: Boolean = true,
    val data: FinishOsData? = null,
    val formPrimaryKey: FormPrimaryKey? = null,
    val isError: Boolean = false,
    val saveFinishOS: Boolean = false,
    var isValidForm: ValidateResult = ValidateResult(false, mutableMapOf())
) : BaseState(translateMap) {
    data class FormPrimaryKey(
        val typeCode: Int,
        val code: Int,
        val versionCode: Int,
        val formData: Long
    )
}


open class BaseState(
    open val translateMap: TranslateMap
)