package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TicketCreationParams(
    @SerializedName("customer_code") var customerCode: Long,
    @SerializedName("type_code") var typeCode: Int,
    @SerializedName("site_code") var siteCode: Int,
    @SerializedName("operation_code") var operationCode: Long,
    @SerializedName("product_code") var productCode: Int,
    @SerializedName("serial_code") var serialCode: Int,
    @SerializedName("comments") var comments: String
) {

}
