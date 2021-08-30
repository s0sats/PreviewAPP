package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MdItemCheck(
    @SerializedName("customer_code") val customerCode: Long,
    @SerializedName("item_check_code") val itemCheckCode: Int,
    @SerializedName("item_check_id") val itemCheckId: String,
    @SerializedName("item_check_desc") val itemCheckDesc: String
)