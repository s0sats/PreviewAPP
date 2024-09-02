package com.namoadigital.prj001.model.region

import com.google.gson.annotations.SerializedName

data class MDRegion(
    @SerializedName("customer_code") val customerCode: Int,
    @SerializedName("region_code") val code: Int,
    @SerializedName("region_id") val id: String,
    @SerializedName("region_desc") val desc: String
)
