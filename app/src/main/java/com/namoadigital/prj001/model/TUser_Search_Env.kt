package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TUser_Search_Env(
    @SerializedName("profile_check") val profileCheck: String,
    @SerializedName("user_code_sql") val userCode: String,
    @SerializedName("email_p") val email: String,
    @SerializedName("user_name") val name: String,
    @SerializedName("erp_code") val erpCode: String,
    appCode: String,
    appVersion: String?,
    appType: String?,
    sessionApp: String?
) : Main_Header_Env(appCode, appVersion, appType, sessionApp ) {

}
