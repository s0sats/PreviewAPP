package com.namoadigital.prj001.core.data.remote.domain

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.util.Constant

data class ApiRequest<T>(
    @SerializedName("token") var token: String,
    @SerializedName("parameters") var parameters: T? = null
) : Main_Header_Env() {

    init {
        app_code = Constant.PRJ001_CODE
        app_version = Constant.PRJ001_VERSION
        app_type = Constant.PKG_APP_TYPE_DEFAULT
    }

}

data class APIWithoutToken<T>(
    @SerializedName("parameters") var parameters: T? = null
) : Main_Header_Env() {
    init {
        app_code = Constant.PRJ001_CODE
        app_version = Constant.PRJ001_VERSION
        app_type = Constant.PKG_APP_TYPE_DEFAULT
    }
}

data class ApiPageRequest<T>(
    @SerializedName("pageSize") val pageSize: Int = 9999,
    @SerializedName("pageNumber") val pageNumber: Int = 1,
    @SerializedName("parameters") var parameters: T? = null
) : Main_Header_Env() {
    init {
        app_code = Constant.PRJ001_CODE
        app_version = Constant.PRJ001_VERSION
        app_type = Constant.PKG_APP_TYPE_DEFAULT
    }
}

