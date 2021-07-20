package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TWorkgroupObj(
    @SerializedName("customer_code")
    val customerCode: Int,
    @SerializedName("group_code")
    val groupCode:Int,
    @SerializedName("group_desc")
    val groupDesc: String,
    @SerializedName("group_image")
    val groupImage: String?,
    @SerializedName("date_expire")
    val dateExpire: String?,
    @SerializedName("active")
    var active : Int
) : Serializable