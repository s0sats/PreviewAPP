package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class MdProductSerialTpDeviceItemHistMat(
    @SerializedName("material_code") var material_code: Int,
    @SerializedName("un") var un: String,
    @SerializedName("qty") var qty: Double,
    @SerializedName("qty_planned") var qty_planned: Double,
    @SerializedName("material_action") var material_action: Int
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
    @SerializedName("seq")
    var seq: Int = -1
        private set

    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        item_check_code: Int,
        item_check_seq: Int,
        seq: Int,
        material_code: Int,
        un: String,
        qty: Double,
        qty_planned: Double,
        material_action: Int
        ) : this(
        material_code,
        un,
        qty,
        qty_planned,
        material_action

    ){
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
        this.device_tp_code = device_tp_code
        this.item_check_code = item_check_code
        this.item_check_seq = item_check_seq
        this.seq = seq
    }

    fun setPk(item: MD_Product_Serial_Tp_Device_Item_Hist){
        this.customer_code = item.customer_code
        this.product_code = item.product_code
        this.serial_code = item.serial_code
        this.device_tp_code = item.device_tp_code
        this.item_check_code = item.item_check_code
        this.item_check_seq = item.item_check_seq
        this.seq = item.seq
    }

    companion object {
        @JvmStatic
        fun getSerialDeviceTpItemHistMaterialFromList(
            serial: MD_Product_Serial,
            deviceItemHistMatList: List<MdProductSerialTpDeviceItemHistMat>
        ): ArrayList<MdProductSerialTpDeviceItemHistMat> {
            return deviceItemHistMatList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }

}
