package com.namoadigital.prj001.model

class GeOsDevice(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val product_code: Int,
    val serial_code: Int,
    val device_tp_code: Int,
    val device_tp_id: String,
    val device_tp_desc: String,
    val order_seq: Int,
    val tracking_number: String?
) {
    fun getGeOsDevicePkPrefix(): String{
        return "${customer_code}.${custom_form_type}.${custom_form_code}.${custom_form_version}.${custom_form_data}.${product_code}.${serial_code}.${device_tp_code}"
    }
}