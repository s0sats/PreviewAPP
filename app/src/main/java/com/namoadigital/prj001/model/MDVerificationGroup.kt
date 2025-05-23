package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MDVerificationGroup(
    @SerializedName("customer_code") var customerCode: Int,
    @SerializedName("vg_code") var vgCode: Int,
    @SerializedName("vg_id") var vgId: String,
    @SerializedName("vg_desc") var vgDesc: String,
)