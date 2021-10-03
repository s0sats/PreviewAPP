package com.namoadigital.prj001.model

class GeOsDeviceItemHist(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val product_code: Int,
    val serial_code: Int,
    val device_tp_code: Int,
    val item_check_code: Int,
    val item_check_seq: Int,
    val seq: Int,
    val exec_type: String,
    val exec_value: Float,
    val exec_date: String,
    val exec_comment: String?,
    val exec_material: Int
) {
}