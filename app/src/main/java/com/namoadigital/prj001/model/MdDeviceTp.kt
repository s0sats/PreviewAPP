package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MdDeviceTp(
    @SerializedName("customer_code") val customerCode: Long,
    @SerializedName("device_tp_code") val deviceTpCode: Int,
    @SerializedName("device_tp_id") val deviceTpId: String,
    @SerializedName("device_tp_desc") val deviceTpDesc: String
)