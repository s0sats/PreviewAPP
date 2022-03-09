package com.namoadigital.prj001.model

class MD_Product_Serial_Tp_Device_Item_Material(
    val material_code: Int,
    val material_qty: Float
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
        material_code: Int,
        material_qty: Float
    ) : this(
        material_code,
        material_qty
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

    companion object{
        @JvmStatic
        fun getSerialDeviceTpItemMaterialFromList(serial: MD_Product_Serial, deviceItemMaterialList: ArrayList<MD_Product_Serial_Tp_Device_Item_Material>) : ArrayList<MD_Product_Serial_Tp_Device_Item_Material> {
            return deviceItemMaterialList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }
}