package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MD_Product_Serial_Tp_Device_Item_Hist(
    @SerializedName("seq") val seq: Int,
    @SerializedName("exec_type") val exec_type: String,
    @SerializedName("exec_value") val exec_value: Double,
    @SerializedName("exec_date") val exec_date: String,
    @SerializedName("exec_comment") val exec_comment: String?,
    @SerializedName("exec_material") val exec_material: Int,
    @SerializedName("change_adjust") val change_adjust: Int,
) {
    @SerializedName("customer_code")
    var customer_code: Long = -1
        private set
    @SerializedName("product_code")
    var product_code: Long = -1
        private set
    @SerializedName("serial_code")
    var serial_code: Long = -1
        private set
    @SerializedName("device_tp_code")
    var device_tp_code: Int = -1
        private set
    @SerializedName("item_check_code")
    var item_check_code: Int = -1
        private set
    @SerializedName("item_check_seq")
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
        exec_value: Double,
        exec_date: String,
        exec_comment: String?,
        exec_material: Int,
        change_adjust: Int
    ) : this(
        seq,
        exec_type,
        exec_value,
        exec_date,
        exec_comment,
        exec_material,
        change_adjust
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
        fun getSerialDeviceTpItemHistFromList(serial: MD_Product_Serial, deviceItemHistList: ArrayList<MD_Product_Serial_Tp_Device_Item_Hist>) : ArrayList<MD_Product_Serial_Tp_Device_Item_Hist> {
            return deviceItemHistList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }
}