package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class TUserWorkgroupObj(
    @SerializedName("user_code")
    var userCode: Int,
    @SerializedName("user_nick")
    var userNick: String,
    @SerializedName("user_name")
    var userName: String,
    @SerializedName("email_p")
    var emailP: String?,
    @SerializedName("user_image")
    var userImage: String?,
    @SerializedName("erp_code")
    var erpCode: String?
)
