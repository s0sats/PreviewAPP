package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.util.*

class T_MD_Product_Serial_Structure : Main_Header_Env() {

    @SerializedName("structure") private var structure = ArrayList<T_MD_Product_Serial_Structure_Env>()
    @SerializedName("zip_mode") private var zip_mode = 0

    fun getSearch(): ArrayList<T_MD_Product_Serial_Structure_Env> {
        return structure
    }

    fun setSearch(search: ArrayList<T_MD_Product_Serial_Structure_Env>) {
        this.structure = search
    }
    fun getZipMode(): Int {
        return zip_mode
    }
    fun setZipMode(zip_mode: Int) {
        this.zip_mode = zip_mode
    }

}
