package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_TK_Ticket_Search_Not_Focus_Param(
        @SerializedName("customer_code") val customer_code: Long,
        @SerializedName("contract_id") val contract_id: String,
        @SerializedName("client_id") val client_id: String,
        @SerializedName("ticket_id") val ticket_id: String,
        @SerializedName("product_code") val product_code: Int?,
        @SerializedName("serial_code") val serial_code: Int?
) {

}
