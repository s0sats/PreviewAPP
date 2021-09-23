package com.namoadigital.prj001.model

class GeOs(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    var custom_form_data: Int,
    var order_type_code: Int,
    var order_type_id: String,
    var order_type_desc: String,
    var backup_product_code: Int?,
    var backup_serial_code: Int?,
    var measure_tp_code: Int,
    var measure_tp_id: String,
    var measure_tp_desc: String,
    var measure_value: Int?,
    var measure_cycle_value: Int?,
    var last_measure_value: Float?,
    var last_measure_date: String?
)