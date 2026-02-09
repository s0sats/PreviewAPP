package com.namoadigital.prj001.model.masterdata.ge_os

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.Serializable

class GeOs(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("custom_form_type") val custom_form_type: Int,
    @SerializedName("custom_form_code") val custom_form_code: Int,
    @SerializedName("custom_form_version") val custom_form_version: Int,
    @SerializedName("custom_form_data") var custom_form_data: Int,
    @SerializedName("order_type_code") var order_type_code: Int,
    @SerializedName("order_type_id") var order_type_id: String,
    @SerializedName("order_type_desc") var order_type_desc: String,
    @SerializedName("process_type") var process_type: String, // MdOrderType
    @SerializedName("display_option") var display_option: String, // MdOrderType
    @SerializedName("item_check_group_code") var item_check_group_code: Int?, // MdOrderType
    @SerializedName("process_vg") var process_vg: String?, // MdOrderType
    @SerializedName("backup_product_code") var backup_product_code: Int?,
    @SerializedName("backup_product_id") var backup_product_id: String?,
    @SerializedName("backup_product_desc") var backup_product_desc: String?,
    @SerializedName("backup_serial_code") var backup_serial_code: Int?,
    @SerializedName("backup_serial_id") var backup_serial_id: String?,
    @SerializedName("measure_tp_code") var measure_tp_code: Int?,
    @SerializedName("measure_tp_id") var measure_tp_id: String?,
    @SerializedName("measure_tp_desc") var measure_tp_desc: String?,
    @SerializedName("measure_value") var measure_value: Float?,
    @SerializedName("measure_cycle_value") var measure_cycle_value: Float?,
    @SerializedName("value_sufix") var value_sufix: String?, //MeMeasure
    @SerializedName("restriction_decimal") var restriction_decimal: Int?, //MeMeasure
    @SerializedName("value_cycle_size") var value_cycle_size: Float?,//MeMeasure
    @SerializedName("cycle_tolerance") var cycle_tolerance: Int?,//MeMeasure
    @SerializedName("date_start") var date_start: String?,
    @SerializedName("date_end") var date_end: String? = null,
    @SerializedName("last_measure_value") val last_measure_value: Float?,
    @SerializedName("last_measure_date") val last_measure_date: String?,
    @SerializedName("last_cycle_value") val last_cycle_value: Float?,
    @SerializedName("so_edit_start_end") val so_edit_start_end: Int,
    @SerializedName("so_order_type_code_default") val so_order_type_code_default: Int?,
    @SerializedName("so_allow_change_order_type") val so_allow_change_order_type: Int,
    @SerializedName("so_allow_backup") val so_allow_backup: Int,
    @SerializedName("device_tp_code_main") val device_tp_code_main: Int?,
    @SerializedName("initial_is_serial_stopped") var initial_is_serial_stopped: Int? = null,
    @SerializedName("initial_stopped_date") var initial_stopped_date: String? = null,
    @SerializedName("initial_unavailability_reason") var initial_unavailability_reason: String? = null,
    @SerializedName("final_is_serial_stopped") var final_is_serial_stopped: Int? = null,
    @SerializedName("final_unavailability_reason") var final_unavailability_reason: String? = null,
    @SerializedName("allow_form_in_the_past") var allowFormInThePast: Int = 0,
) : Serializable {

//    val hasForcedExpiredVg = force_exe_expired_vg == 1

    fun getFormPK(): FormPK {
        return FormPK(
            typeCode = custom_form_type,
            code = custom_form_code,
            versionCode = custom_form_version,
            formData = custom_form_data
        )
    }

    fun getProcessVg(): ProcessVg? {
        return ProcessVg.fromString(process_vg)
    }

    /**
     *  BARRIONUEVO - 11-=08-2022
     *  METODO QUE TRAZ O MAIOR ENTRE CICLO CONSIDERADO E VALOR INSERIDO PELO USER.
     */
    fun maxMeasureValue(): Float {
        val mMeasure_value = measure_value ?: 0f
        val mMeasure_cycle_value = measure_cycle_value ?: 0f
        return if (mMeasure_value > mMeasure_cycle_value) {
            mMeasure_value
        } else {
            mMeasure_cycle_value
        }
    }

    fun getDateConsider(): String? {
        val dateLastMinute = ToolBox_Inf.getDateLastMinute(date_start)
        if(dateLastMinute.contains("1900")){
            return null
        }
        return dateLastMinute
    }

    fun getMeasureConsider() = if (measure_cycle_value != null && measure_cycle_value!!.compareTo(-1f) > 0) {
            measure_cycle_value!!
        } else {
            0f
        }

    data class FormPK(
        val typeCode: Int,
        val code: Int,
        val versionCode: Int,
        val formData: Int,
    )

}