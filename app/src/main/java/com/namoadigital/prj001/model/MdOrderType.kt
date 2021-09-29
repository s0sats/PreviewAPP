package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MdOrderType(
    @SerializedName("customer_code") val customerCode: Long,
    @SerializedName("order_type_code") val orderTypeCode: Int,
    @SerializedName("order_type_id") val orderTypeId: String,
    @SerializedName("order_type_desc") val orderTypeDesc: String,
    @SerializedName("process_type") val processType: String,
    @SerializedName("display_option") val displayOption: String
){
   companion object ProcessType{
        const val PREVENTIVE = "PREVENTIVE"
        const val DAMAGE = "DAMAGE"
        const val CORRECTIVE = "CORRECTIVE"
        const val CORRECTIVE_PLANNED = "CORRECTIVE_PLANNED"
    }
}

