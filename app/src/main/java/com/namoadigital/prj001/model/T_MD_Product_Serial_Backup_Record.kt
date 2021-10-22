package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_MD_Product_Serial_Backup_Record(
    @SerializedName("customer_code")
    val customerCode:Int,
    @SerializedName("product_code")
    val productCode:Int,
    @SerializedName("product_id")
    val productId:String,
    @SerializedName("product_desc")
    val productDesc:String,
    @SerializedName("serial_code")
    val serialCode:Int,
    @SerializedName("serial_id")
    val serialId:String,
    @SerializedName("site_code")
    val siteCode:Int?,
    @SerializedName("site_id")
    val siteId:String?,
    @SerializedName("site_desc")
    val siteDesc:String?
)
