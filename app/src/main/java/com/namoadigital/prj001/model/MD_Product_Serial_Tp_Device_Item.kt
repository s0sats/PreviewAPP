package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MD_Product_Serial_Tp_Device_Item(
    @SerializedName("item_check_code") val item_check_code: Int,
    @SerializedName("item_check_seq") val item_check_seq: Int,
    @SerializedName("apply_material") val apply_material: String,
    @SerializedName("verification_instruction") val verification_instruction: String?,
    @SerializedName("require_justify_problem") val require_justify_problem: Int,
    @SerializedName("critical_item") val critical_item: Int,
    @SerializedName("order_seq") val order_seq: Int,
    @SerializedName("structure") val structure: Int,
    @SerializedName("manual_desc") val manual_desc: String?,
    @SerializedName("next_cycle_measure") val next_cycle_measure: Double?,
    @SerializedName("next_cycle_measure_date") val next_cycle_measure_date: String?,
    @SerializedName("next_cycle_limit_date") val next_cycle_limit_date: String?,
    @SerializedName("item_check_status") val item_check_status: String,
    @SerializedName("target_date") val target_date : String?
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
    var material = mutableListOf<MD_Product_Serial_Tp_Device_Item_Material>()

    /**
     * QUANDO OBJ GERADO VIA GSON NÃO PASSA NO INIT E LISTA NASCE NULL
     */
    init {
        hist = mutableListOf<MD_Product_Serial_Tp_Device_Item_Hist>()
        material = mutableListOf<MD_Product_Serial_Tp_Device_Item_Material>()
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
        this.material?.forEach {
            it.setPk(this)
        }
    }

    companion object {
        const val APPLY_MATERIAL_NO = "NO"
        const val APPLY_MATERIAL_REQUIRED = "REQUIRED"
        const val APPLY_MATERIAL_OPTIONAL = "OPTIONAL"
        @JvmStatic
        fun getSerialDeviceTpItemFromList(serial: MD_Product_Serial, deviceItemList: ArrayList<MD_Product_Serial_Tp_Device_Item>) : ArrayList<MD_Product_Serial_Tp_Device_Item> {
            return deviceItemList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }
}