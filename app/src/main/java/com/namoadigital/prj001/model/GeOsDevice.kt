package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class GeOsDevice(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("custom_form_type") val custom_form_type: Int,
    @SerializedName("custom_form_code") val custom_form_code: Int,
    @SerializedName("custom_form_version") val custom_form_version: Int,
    @SerializedName("custom_form_data") val custom_form_data: Int,
    @SerializedName("product_code") val product_code: Int,
    @SerializedName("serial_code") val serial_code: Int,
    @SerializedName("device_tp_code") val device_tp_code: Int,
    @SerializedName("device_tp_id") val device_tp_id: String,
    @SerializedName("device_tp_desc") val device_tp_desc: String,
    @SerializedName("order_seq") val order_seq: Int,
    @SerializedName("tracking_number") val tracking_number: String?,
    @SerializedName("show_empty") val show_empty: Int

) {
    fun getGeOsDevicePkPrefix(): String{
        return "${customer_code}.${custom_form_type}.${custom_form_code}.${custom_form_version}.${custom_form_data}.${product_code}.${serial_code}.${device_tp_code}"
    }
}