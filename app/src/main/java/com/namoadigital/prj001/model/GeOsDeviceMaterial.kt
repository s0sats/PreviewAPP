package com.namoadigital.prj001.model

import java.io.Serializable

class GeOsDeviceMaterial(
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
    val material_code: Int,
    val material_id: String,
    val material_desc: String,
    val material_qty: Float,
    val material_unit: String?
): Serializable