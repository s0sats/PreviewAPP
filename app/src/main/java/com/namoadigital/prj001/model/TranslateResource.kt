package com.namoadigital.prj001.model

import android.content.Context
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

data class TranslateResource(
    val context: Context,
    val mModule_code: String = ConstantBaseApp.APP_MODULE,
    val mResoure_code: String,
) {
    fun setLanguage(list: List<String>) = ToolBox_Inf.setLanguage(
        context,
        mModule_code,
        mResoure_code,
        ToolBox_Con.getPreference_Translate_Code(context),
        list
    )
}
