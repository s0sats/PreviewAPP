package com.namoadigital.prj001.ui.act070.model

data class SerialSearch(
    var serialCode: Int? = null,
    var serialId: String? = null,
    var productCode: Int? = null,
){
    fun clear(){
        serialCode = null
        serialId = null
        productCode = null
    }

    fun isEmpty() : Boolean {
        return serialCode == null && serialId == null && productCode == null
    }
}