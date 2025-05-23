package com.namoadigital.prj001.ui.act093.model

data class DeviceTpModel(
    val customer_code: Int = 0,
    val product_code: Int = 0,
    val serial_code: Int = 0,
    val device_tp_code: Int = 0,
    val device_tp_desc: String = "",
    val item_check_code: Int = 0,
    val item_check_seq: Int = 0,
    val item_check_desc: String = "",
    val item_check_status: String = "",
    val critical_item: Int = 0,
    val materialListFormatted: String = ""
)