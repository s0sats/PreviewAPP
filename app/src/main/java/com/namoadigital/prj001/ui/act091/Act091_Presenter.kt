package com.namoadigital.prj001.ui.act091

import android.os.Bundle
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.model.TSO_Service_Search_Rec
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.ArrayList

class Act091_Presenter constructor(
    private val context: Context,
    private val mModule_code: String,
    private val mResource_code: String,
    private val bundle: Bundle
) : Act91_Contract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act091_title",
            "empty_list_lbl",
            "filter_hint",
            "insert_filter_placeholder"
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_code,
            mResource_code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }


    override fun getTranslation() = hmAuxTrans


    override fun getListData(): List<TSO_Service_Search_Obj> {

        val gson = GsonBuilder().serializeNulls().create()
        val contents = ToolBox_Inf.getContents(
            File(ConstantBaseApp.SO_EXPRESS_JSON_PATH,
                ToolBox_Inf.getExpressSOFileName(
                    bundle.getInt(SO_Pack_ExpressDao.CONTRACT_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.PRODUCT_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.CATEGORY_PRICE_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.SITE_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.OPERATION_CODE)
                )
            )
        )
        val list = gson.fromJson(
            contents,
            TSO_Service_Search_Rec::class.java
        )

        return list.data
    }

}