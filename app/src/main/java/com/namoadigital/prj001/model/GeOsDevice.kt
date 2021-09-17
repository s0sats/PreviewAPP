package com.namoadigital.prj001.model

class GeOsDevice(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val product_code: Int,
    val serial_code: Int,
    var device_tp_code: Int,
    var device_tp_id: String,
    var device_tp_desc: String,
    var order_seq: Int,
    var tracking_number: String?
) {
}