package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MD_Product_Serial_Tp_Device_Item(
    @SerializedName("item_check_code") val item_check_code: Int,
    @SerializedName("item_check_seq") val item_check_seq: Int,
    @SerializedName("apply_material") val apply_material: String,
    @SerializedName("verification_instruction") val verification_instruction: String?,
    @SerializedName("require_justify_problem") val require_justify_problem: Int,
    @SerializedName("critical_item") val critical_item: Int,
    @SerializedName("change_adjust") val change_adjust: Int,
    @SerializedName("order_seq") val order_seq: Int,
    @SerializedName("structure") val structure: Int,
    @SerializedName("already_ok_hide") val already_ok_hide: Int,
    @SerializedName("require_photo_fixed") val require_photo_fixed: Int,
    @SerializedName("require_photo_alert") val require_photo_alert: Int,
    @SerializedName("require_photo_already_ok") val require_photo_already_ok: Int,
    @SerializedName("require_photo_not_verified") val require_photo_not_verified: Int,
    @SerializedName("vg_code") val vg_code: Int?,
    @SerializedName("manual_desc") val manual_desc: String?,
    @SerializedName("next_cycle_measure") val next_cycle_measure: Double?,
    @SerializedName("next_cycle_measure_date") val next_cycle_measure_date: String?,
    @SerializedName("next_cycle_limit_date") val next_cycle_limit_date: String?,
    @SerializedName("item_check_status") val item_check_status: String,
    @SerializedName("target_date") val target_date: String?,
    @SerializedName("partitioned_execution") val partitioned_execution: Int,
    @SerializedName("ticket_prefix") val ticket_prefix: Int?,
    @SerializedName("ticket_code") val ticket_code: Int?,
    @SerializedName("vg_action") val vg_action: Int,

    //Measure Item
    @SerializedName("measure_active") val measureActive: Int? = null,
    @SerializedName("measure_require_id") val measureRequireId: Int? = null,
    @SerializedName("measure_un") val measureUn: String? = null,
    @SerializedName("measure_min") val measureMin: Double? = null,
    @SerializedName("measure_max") val measureMax: Double? = null,
    @SerializedName("measure_alert_min") val measureAlertMin: Double? = null,
    @SerializedName("measure_alert_max") val measureAlertMax: Double? = null,
    @SerializedName("last_measure_value") val lastMeasureValue: Double? = null,
    @SerializedName("last_measure_id") val lastMeasureId: String? = null,
    @SerializedName("last_measure_un") val lastMeasureUn: String? = null,
    @SerializedName("last_measure_date") val lastMeasureDate: String? = null,
    @SerializedName("last_measure_alert") val lastMeasureAlert: Int? = null,
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

    @SerializedName("hist")
    var hist = mutableListOf<MD_Product_Serial_Tp_Device_Item_Hist>()

    @SerializedName("material")
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
        change_adjust: Int,
        order_seq: Int,
        structure: Int,
        already_ok_hide: Int,
        require_photo_fixed: Int,
        require_photo_alert: Int,
        require_photo_already_ok: Int,
        require_photo_not_verified: Int,
        vg_code: Int?,
        manual_desc: String?,
        next_cycle_measure: Double?,
        next_cycle_measure_date: String?,
        next_cycle_limit_date: String?,
        item_check_status: String,
        target_date: String?,
        partitioned_execution: Int,
        ticket_prefix: Int?,
        ticket_code: Int?,
        vg_action: Int,
        measureActive: Int?,
        measureRequireId: Int?,
        measureUn: String?,
        measureMin: Double?,
        measureMax: Double?,
        measureAlertMin: Double?,
        measureAlertMax: Double?,
        lastMeasureValue: Double?,
        lastMeasureId: String?,
        lastMeasureUn: String?,
        lastMeasureDate: String?,
        lastMeasureAlert: Int?,
    ) : this(
        item_check_code,
        item_check_seq,
        apply_material,
        verification_instruction,
        require_justify_problem,
        critical_item,
        change_adjust,
        order_seq,
        structure,
        already_ok_hide,
        require_photo_fixed,
        require_photo_alert,
        require_photo_already_ok,
        require_photo_not_verified,
        vg_code,
        manual_desc,
        next_cycle_measure,
        next_cycle_measure_date,
        next_cycle_limit_date,
        item_check_status,
        target_date,
        partitioned_execution,
        ticket_prefix,
        ticket_code,
        vg_action,
        measureActive,
        measureRequireId,
        measureUn,
        measureMin,
        measureMax,
        measureAlertMin,
        measureAlertMax,
        lastMeasureValue,
        lastMeasureId,
        lastMeasureUn,
        lastMeasureDate,
        lastMeasureAlert,
    ) {
        this.customer_code = customer_code
        this.product_code = product_code
        this.serial_code = serial_code
        this.device_tp_code = device_tp_code
    }

    fun setPk(device: MD_Product_Serial_Tp_Device) {
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
        fun getSerialDeviceTpItemFromList(
            serial: MD_Product_Serial,
            deviceItemList: ArrayList<MD_Product_Serial_Tp_Device_Item>
        ): ArrayList<MD_Product_Serial_Tp_Device_Item> {
            return deviceItemList.filter {
                it.customer_code == serial.customer_code
                        && it.product_code == serial.product_code
                        && it.serial_code == serial.serial_code
            } as ArrayList
        }
    }
}