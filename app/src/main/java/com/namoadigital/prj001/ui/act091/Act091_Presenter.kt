package com.namoadigital.prj001.ui.act091

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.model.TSO_Service_Search_Rec
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import java.util.ArrayList

class Act091_Presenter constructor(val bundle: Bundle) : Act91_Contract.I_Presenter {


    override fun getListData(): List<TSO_Service_Search_Obj> {

        val gson = GsonBuilder().serializeNulls().create()
        val contents = ToolBox_Inf.getContents(
            File(ConstantBaseApp.SO_EXPRESS_JSON_PATH,
                ToolBox_Inf.getExpressSOFileName(
                    bundle.getInt(SO_Pack_ExpressDao.CONTRACT_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.PRODUCT_CODE),
                    bundle.getInt(SO_Pack_ExpressDao.CATEGORY_PRICE_CODE)
                )
            )
        )
        val list = gson.fromJson(
            contents,
            TSO_Service_Search_Rec::class.java
        )

        return list.data
    }

    private val getData = LIST_EXAMPLE_ACT091
}