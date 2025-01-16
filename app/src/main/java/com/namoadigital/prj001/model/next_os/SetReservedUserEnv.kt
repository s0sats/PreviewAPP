package com.namoadigital.prj001.model.next_os

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.util.Constant

data class SetReservedUserEnv (
    @SerializedName("token") val token : String,
    @SerializedName("so_prefix") val so_prefix: Int,
    @SerializedName("so_code") val so_code: Int,
    @SerializedName("so_scn") val so_scn: Int,
    @SerializedName("reserved_user") val reserved_user : Int?,
) : Main_Header_Env() {
    init {
        app_code = Constant.PRJ001_CODE
        app_version = Constant.PRJ001_VERSION
        app_type = Constant.PKG_APP_TYPE_DEFAULT
    }
}
