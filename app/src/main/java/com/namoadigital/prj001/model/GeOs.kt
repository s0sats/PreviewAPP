package com.namoadigital.prj001.model

import java.io.Serializable

class GeOs(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    var custom_form_data: Int,
    var order_type_code: Int,
    var order_type_id: String,
    var order_type_desc: String,
    var process_type: String, // MdOrderType
    var display_option: String, // MdOrderType
    var backup_product_code: Int?,
    var backup_product_id: String?,
    var backup_product_desc: String?,
    var backup_serial_code: Int?,
    var backup_serial_id: String?,
    var measure_tp_code: Int?,
    var measure_tp_id: String?,
    var measure_tp_desc: String?,
    var measure_value: Float?,
    var measure_cycle_value: Float?,
    var value_sufix: String?, //MeMeasure
    var restriction_decimal: Int?, //MeMeasure
    var value_cycle_size: Float?,//MeMeasure
    var cycle_tolerance: Int?,//MeMeasure
    var date_start: String?,
    var date_end: String? = null,
    val last_measure_value: Float?,
    val last_measure_date: String?,
    val last_cycle_value: Float?,
    val so_edit_start_end: Int,
    val so_order_type_code_default: Int?,
    val so_allow_change_order_type: Int,
    val so_allow_backup: Int,
    val device_tp_code_main: Int?
):Serializable