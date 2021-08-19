package com.namoadigital.prj001.ui.act086

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act086MainPresenter(
    private val context: Context,
    private val mView: Act086MainContract.I_View,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String
) : Act086MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun getTranslation() = hmAuxTrans

    private fun loadTranslation() : HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act086_title",
            "product_ttl",
            "btn_apply",
            "alert_choose_an_answer_msg"
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

}