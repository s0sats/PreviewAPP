package com.namoadigital.prj001.ui.act091

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.model.TSO_Service_Search_Rec
import java.util.ArrayList

class Act091_Presenter constructor() : Act91_Contract.I_Presenter {


    override fun getListData(): List<TSO_Service_Search_Obj> {

        val gson = GsonBuilder().serializeNulls().create()
        val list = gson.fromJson(
            getData,
            TSO_Service_Search_Rec::class.java
        )

        return list.data
    }

    private val getData = LIST_EXAMPLE_ACT091
}