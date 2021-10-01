package com.namoadigital.prj001.model

class MD_Product_Serial_Tp_Device_Item(
    val item_check_code: Int,
    val item_check_seq: Int,
    val apply_material: String,
    val verification_instruction: String?,
    val require_justify_problem: Int,
    val critical_item: Int,
    val order_seq: Int,
    val structure: Int,
    val manual_desc: String?,
    val next_cycle_measure: Double?,
    val next_cycle_measure_date: String?,
    val next_cycle_limit_date: String?,
    val item_check_status: String,
    val target_date : String?
) {
    var customer_code: Long = -1
        private set
    var product_code: Long = -1
        private set
    var serial_code: Long = -1
        private set
    var device_tp_code: Int = -1
        private set
    var hist = mutableListOf<MD_Product_Serial_Tp_Device_Item_Hist>()

    /**
     * QUANDO OBJ GERADO VIA GSON NÃO PASSA NO INIT E LISTA NASCE NULL
     */
    init {
        hist = mutableListOf<MD_Product_Serial_Tp_Device_Item_Hist>()
    }

    constructor(
        customer_code: Long,
        product_code: Long,
        serial_code: Long,
        device_tp_code: Int,
        item_check_code: Int,
        item_check_seq: Int,
        apply_material: String,
        verification_instruction: String?,
        require_justify_problem: Int,
        critical_item: Int,
        order_seq: Int,
        structure: Int,
        manual_desc: String?,
        next_cycle_measure: Double?,
        next_cycle_measure_date: String?,
        next_cycle_limit_date: String?,
        item_check_status: String,
        target_date : String?
    ) : this(
        item_check_code,
        item_check_seq,
        apply_material,
        verification_instruction,
        require_justify_problem,
        critical_item,
        order_seq,
        structure,
        manual_desc,
        next_cycle_measure,
        next_cycle_measure_date,
        next_cycle_limit_date,
        item_check_status,
        target_date
    ) {
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
        this.device_tp_code = device_tp_code
    }

    fun setPk(device: MD_Product_Serial_Tp_Device){
        this.customer_code = device.customer_code
        this.product_code = device.product_code
        this.serial_code = device.serial_code
        this.device_tp_code = device.device_tp_code
        //
        this.hist.forEach {
            it.setPk(this)
        }
    }

}