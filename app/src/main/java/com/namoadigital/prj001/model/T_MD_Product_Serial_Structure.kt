package com.namoadigital.prj001.model

import java.util.*

class T_MD_Product_Serial_Structure : Main_Header_Env() {
    private var structure = ArrayList<T_MD_Product_Serial_Structure_Env>()

    fun getSearch(): ArrayList<T_MD_Product_Serial_Structure_Env> {
        return structure
    }

    fun setSearch(search: ArrayList<T_MD_Product_Serial_Structure_Env>) {
        this.structure = search
    }
}
