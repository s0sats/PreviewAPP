package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class TkTicketTypeProduct(
@SerializedName("customer_code") val customer_code: Long,
@SerializedName("type_code") val ticket_type_code: Int,
@SerializedName("product_code") val product_code: Int
)