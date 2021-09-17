package com.namoadigital.prj001.model

class GeOs(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val order_type_code: Int,
    val order_type_id: String,
    val order_type_desc: String,
    val backup_product_code: Int?,
    val backup_serial_code: Int?,
    val measure_tp_code: Int,
    val measure_tp_id: String,
    val measure_tp_desc: String,
    val measure_value: Int?,
    val measure_cycle_value: Int?,
    val last_measure_value: Float?,
    val last_measure_date: String?
) {
}