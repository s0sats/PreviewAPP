package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class T_MD_Product_Serial_Backup_Rec(
    @SerializedName("app")
    @Expose
    val app:String,
    @SerializedName("validation")
    @Expose
    val validation:String,
    @SerializedName("link_url")
    @Expose
    val link_url:String,
    @SerializedName("error_msg")
    @Expose
    val error_msg:String,
    @SerializedName("record")
    @Expose
    val records: List<T_MD_Product_Serial_Backup_Record>?
) {
}