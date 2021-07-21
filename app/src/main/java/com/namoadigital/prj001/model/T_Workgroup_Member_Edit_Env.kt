package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_Workgroup_Member_Edit_Env(
    app_code: String,
    app_version: String,
    app_type: String,
    session_app: String,
    @SerializedName("data")
    val data: WorkgroupSetData
): Main_Header_Env(
     app_code,
     app_version,
     app_type,
     session_app
){
    class WorkgroupSetData(
        @SerializedName("user_code")
        val userCode: Int,
        @SerializedName("active")
        val active: Int,
        @SerializedName("limite")
        val limit: Int = 0,
        @SerializedName("date_expire")
        val dateExpire: String? = null,
        @SerializedName("expire_return")
        val expireReturn: Int? = null,
        @SerializedName("group_code")
        val groupCodeList: ArrayList<Int> = arrayListOf()
    ){
        companion object{
            const val USER_CODE = "userCode"
            const val ACTIVE = "active"
            const val LIMIT = "limit"
            const val DATE_EXPIRE = "date_expire"
            const val EXPIRE_RETURN = "expire_return"
            const val GROUP_CODE = "group_code"
        }
    }
}


