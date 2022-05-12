package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class MdTag(
        @SerializedName("customer_code") var customer_code: Int,
        @SerializedName("tag_code") var tag_code: Int,
        @SerializedName("tag_id") var tag_id: String,
        @SerializedName("tag_desc") var tag_desc: String
)