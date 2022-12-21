package com.namoadigital.prj001.ui.act093.model

data class DeviceTpModel(
    val customer_code: Int,
    val product_code: Int,
    val serial_code: Int,
    val device_tp_code: Int,
    val device_tp_desc: String,
    val item_check_code: Int,
    val item_check_seq: Int,
    val item_check_desc: String,
    val item_check_status: String,
    val critical_item: Int,
    val materialListFormatted: String
)

/*
customer_code
product_code
serial_code
device_tp_code
item_check_code
item_check_seq
item_check_status
critical_item
device_tp_desc
List<device_product>
*/



