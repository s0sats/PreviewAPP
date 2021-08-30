package com.namoadigital.prj001.model

class MD_Product_Serial_Tp_Device(
    val device_tp_code: Int,
    val order_seq: Int,
    val tracking_number: String? = null
){
    var customer_code: Long = -1
        private set
    var product_code: Long = -1
        private set
    var serial_code: Long = -1
        private set
    var items : MutableList<MD_Product_Serial_Tp_Device_Item> =  mutableListOf()

    /**
     * QUANDO OBJ GERADO VIA GSON NÃO PASSA NO INIT E LISTA NASCE NULL
     */

    init {
        items = mutableListOf()
    }

    //Construto quando carregado via banco
    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        order_seq: Int,
        tracking_number: String? = null
    ) : this(device_tp_code, order_seq, tracking_number){
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
    }

    fun setPk(serial: MD_Product_Serial){
        this.customer_code = serial.customer_code
        this.product_code = serial.product_code
        this.serial_code = serial.serial_code
    }

}