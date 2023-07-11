package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class SmPriority(
        @SerializedName("customer_code") var customer_code : Long,
        @SerializedName("priority_code") var priority_code : Int,
        @SerializedName("priority_desc") var priority_desc : String,
        @SerializedName("priority_weight") var priority_weight : Int,
        @SerializedName("priority_default") var priority_default : Int,
        @SerializedName("priority_color") var priority_color : String,
)