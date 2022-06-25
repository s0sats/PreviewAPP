package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class TkTicketType(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("type_code") val ticket_type_code: Int,
    @SerializedName("type_id") val ticket_type_id: String,
    @SerializedName("type_desc") val ticket_type_desc: String,
    @SerializedName("all_site") val all_site: Int,
    @SerializedName("all_operation") val all_operation: Int,
    @SerializedName("all_product") val all_product: Int,
    @SerializedName("tag_operational_code") val tag_operational_code: Int
)