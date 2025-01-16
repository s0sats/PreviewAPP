package com.namoadigital.prj001.model.next_os

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.util.Constant

data class ListReservedUserEnv(
    @SerializedName("siteCode") val siteCode: Int,
    @SerializedName("operationCode") val operationCode: Int,
    @SerializedName("segmentCode") val segmentCode: Int,
    @SerializedName("productCode") val productCode: Int,
    @SerializedName("contractCode") val contractCode: Int,
    @SerializedName("active") val active: Int = 1,
) : Main_Header_Env() {
    init {
        app_code = Constant.PRJ001_CODE
        app_version = Constant.PRJ001_VERSION
        app_type = Constant.PKG_APP_TYPE_DEFAULT
    }
}
