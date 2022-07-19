package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class TkTicketTypeSite(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("type_code") val ticket_type_code: Int,
    @SerializedName("site_code") val site_code: Int
)