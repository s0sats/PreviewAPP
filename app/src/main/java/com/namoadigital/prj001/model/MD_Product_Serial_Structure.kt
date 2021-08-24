package com.namoadigital.prj001.model

class MD_Product_Serial_Structure(
    val customer_code: Long,
    val product_code: Int,
    val serial_code: Int,
    val scn_item_check: Int,
    val measure_tp_code: Int?,
    val last_measure_value: Double?,
    val last_measure_date: String?,
    val device_tp: MutableList<MD_Product_Serial_Tp_Device> = mutableListOf()
)