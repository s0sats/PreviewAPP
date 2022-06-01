package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MD_Product_Serial_Tp_Device(
    @SerializedName("device_tp_code") val device_tp_code: Int,
    @SerializedName("order_seq") val order_seq: Int,
    @SerializedName("tracking_number") val tracking_number: String? = null
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
    @SerializedName("item")
    var item: MutableList<MD_Product_Serial_Tp_Device_Item> = mutableListOf()

    /**
     * QUANDO OBJ GERADO VIA GSON NÃO PASSA NO INIT E LISTA NASCE NULL
     */

    init {
        item = mutableListOf()
    }

    //Construto quando carregado via banco
    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        order_seq: Int,
        tracking_number: String? = null
    ) : this(device_tp_code, order_seq, tracking_number) {
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
    }

    fun setPk(serial: MD_Product_Serial) {
        this.customer_code = serial.customer_code
        this.product_code = serial.product_code
        this.serial_code = serial.serial_code
        //
        item.forEach {
            it.setPk(this)
        }
    }

    companion object {
        @JvmStatic
        fun getSerialDeviceTpFromList(
            serial: MD_Product_Serial,
            deviceList: ArrayList<MD_Product_Serial_Tp_Device>
        ): ArrayList<MD_Product_Serial_Tp_Device> {
            return deviceList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }

}