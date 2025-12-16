package com.namoadigital.prj001.model.masterdata.label

import com.google.gson.annotations.SerializedName

data class MDItemCheckLabel(
    @SerializedName("customer_code") val customerCode:Long,
    @SerializedName("label_code") val labelCode:Int,
    @SerializedName("label_type") val labelType:String,
    @SerializedName("label_id") val labelId:String,
    @SerializedName("label_desc") val labelDesc:String,
    @SerializedName("label_icon_id") val labelIconId:String,
    @SerializedName("active") val active:Int
)