package com.namoadigital.prj001.ui.act079.model

import com.google.gson.annotations.SerializedName

abstract class Act079BaseViewNC(
    protected val page: Int,
    protected val customFormDataType: String,
    protected val customFormOrder: Int,
    protected val description: String,
    protected val positionIdx: Int,
    protected val positionY: Int
){

    fun getViewIndex():String{
        return "$page.$customFormOrder"
    }
}