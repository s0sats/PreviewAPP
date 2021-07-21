package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_Workgroup_Member_List_Env(
    app_code: String,
    app_version: String,
    app_type: String,
    session_app: String,
    @SerializedName("user_code_find")
    val userCode: Int
): Main_Header_Env(
     app_code,
     app_version,
     app_type,
     session_app
)
