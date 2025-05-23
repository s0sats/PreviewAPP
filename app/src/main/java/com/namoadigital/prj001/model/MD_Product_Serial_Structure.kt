package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg

class MD_Product_Serial_Structure(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("product_code") val product_code: Long,
    @SerializedName("serial_code") val serial_code: Int,
    @SerializedName("has_item_check") val has_item_check: Int,
    @SerializedName("scn_item_check") val scn_item_check: Int?,
    @SerializedName("measure_tp_code") val measure_tp_code: Int?,
    @SerializedName("last_measure_value") val last_measure_value: Double?,
    @SerializedName("last_measure_date") val last_measure_date: String?,
    @SerializedName("last_cycle_value") val last_cycle_value: Float?,
    @SerializedName("last_cycle_date") val last_cycle_date: String?,
    @SerializedName("horimeter") val  horimeter:Double?,
    @SerializedName("horimeter_date") val  horimeter_date:String?,
    @SerializedName("horimeter_supplier_uid") val  horimeter_supplier_uid:String?,
    @SerializedName("horimeter_supplier_desc") val  horimeter_supplier_desc:String?,
    @SerializedName("measure_block_input_time") val  measure_block_input_time:Long?,
    @SerializedName("measure_alert_input_time") val  measure_alert_input_time:Long?,
    @SerializedName("unavailability_reason_option") val  unavailability_reason_option:Int,
    @SerializedName("vg") val verificationGroup: MutableList<MDProductSerialVg>?,
    @SerializedName("device_tp") val device_tp: MutableList<MD_Product_Serial_Tp_Device> = mutableListOf()
){
    fun setPk(md_product_serial: MD_Product_Serial) {
        for (i in device_tp.indices) {
            device_tp.get(i).setPk(md_product_serial)
        }
    }
}