package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TUnfocusAndHistoricEnv(
    @SerializedName("product_code") val productCode: Int,
    @SerializedName("serial_code") val serialCode: Long,
    appCode: String,
    appVersion: String?,
    appType: String?,
    sessionApp: String?
) : Main_Header_Env(appCode, appVersion, appType, sessionApp ) { 
}