package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MeMeasureTp(
    @SerializedName("customer_code") val customerCode: Long,
    @SerializedName("measure_tp_code") val measureTpCode: Int,
    @SerializedName("measure_tp_id") val measureTpId: String,
    @SerializedName("measure_tp_desc") val measureTpDesc: String,
    @SerializedName("value_sufix") val valueSufix: String?,
    @SerializedName("restriction_type") val restrictionType: String?,
    @SerializedName("restriction_min") val restrictionMin: Int?,
    @SerializedName("restriction_max") val restrictionMax: Int?,
    @SerializedName("restriction_decimal") val restrictionDecimal: Int? = 4,
    @SerializedName("value_cycle_size") val valueCycleSize: Float?,
    @SerializedName("cycle_tolerance") val cycleTolerance: Int?
){

    companion object{
        const val RESTRICTION_TYPE_VALUE = "VALUE"
        const val RESTRICTION_TYPE_VALUE_BY_DAY = "VALUE_BY_DAY"
        const val RESTRICTION_TYPE_MIN_MAX = "MIN_MAX"

    }
}