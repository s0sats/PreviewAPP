package com.namoadigital.prj001.ui.act011.finish_os.ui.utils

data class FinishScreenArguments(
    val typeCode: Int,
    val code: Int,
    val versionCode: Int,
    val formData: Long,
    val isReadOnly: Boolean = false,
) {

    object ARGUMENTS {
        const val TYPE_CODE = "typeCode"
        const val CODE = "code"
        const val VERSION_CODE = "versionCode"
        const val FORM_DATA = "formData"
        const val IS_READ_ONLY = "isReadOnly"
    }
}