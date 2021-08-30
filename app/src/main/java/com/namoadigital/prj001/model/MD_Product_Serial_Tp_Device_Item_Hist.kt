package com.namoadigital.prj001.model

class MD_Product_Serial_Tp_Device_Item_Hist(
    val seq: Int,
    val exec_type: String,
    val exec_measure: Double,
    val exec_date: String,
    val exec_comment: String?,
    val exec_material: Int
) {

    var customer_code: Long = -1
        private set
    var product_code: Long = -1
        private set
    var serial_code: Long = -1
        private set
    var device_tp_code: Int = -1
        private set
    var item_check_code: Int = -1
        private set
    var item_check_seq: Int = -1
        private set

    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        item_check_code: Int,
        item_check_seq: Int,
        seq: Int,
        exec_type: String,
        exec_measure: Double,
        exec_date: String,
        exec_comment: String?,
        exec_material: Int
    ) : this(
        seq,
        exec_type,
        exec_measure,
        exec_date,
        exec_comment,
        exec_material
    ){
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
        this.device_tp_code = device_tp_code
        this.item_check_code = item_check_code
        this.item_check_seq = item_check_seq
    }

    fun setPk(item: MD_Product_Serial_Tp_Device_Item){
        this.customer_code = item.customer_code
        this.product_code = item.product_code
        this.serial_code = item.serial_code
        this.device_tp_code = item.device_tp_code
        this.item_check_code = item.item_check_code
        this.item_check_seq = item.item_check_seq
    }
}