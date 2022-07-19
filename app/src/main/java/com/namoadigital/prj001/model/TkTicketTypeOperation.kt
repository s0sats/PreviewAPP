package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class TkTicketTypeOperation(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("type_code") val ticket_type_code: Int,
    @SerializedName("operation_code") val operation_code: Int
)