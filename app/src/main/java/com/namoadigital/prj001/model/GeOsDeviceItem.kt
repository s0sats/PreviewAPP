package com.namoadigital.prj001.model

import java.io.Serializable

class GeOsDeviceItem(
    val customer_code :Long,
    val custom_form_type :Int,
    val custom_form_code :Int,
    val custom_form_version :Int,
    val custom_form_data :Int,
    val product_code :Int,
    val serial_code :Int,
    val device_tp_code :Int,
    val item_check_code :Int,
    val item_check_seq :Int,
    val item_check_id :String,
    val item_check_desc :String,
    val apply_material :String,
    val verification_instruction :String?,
    val require_justify_problem :Int,
    val critical_item :Int,
    val order_seq :Int,
    val structure :Int,
    val manual_desc :String?,
    val next_cycle_measure :Float?,
    val next_cycle_measure_date :String?,
    val next_cycle_limit_date :String?,
    var item_check_status :String,
    var target_date :String?,
    var exec_type :String?,
    var exec_date :String?,
    var exec_comment :String?,
    var exec_photo1 :String?,
    var exec_photo2 :String?,
    var exec_photo3 :String?,
    var exec_photo4 :String?,
    var status_answer :String?,
    val materialList: MutableList<GeOsDeviceMaterial>  = mutableListOf()
):Serializable {
    fun getGeOsDeviceItemCodeAndSeq(): String{
        return "${item_check_code}.${item_check_seq}"
    }

    companion object{
        const val ITEM_CHECK_STATUS_NO_CYCLE = "NO_CYCLE"
        const val ITEM_CHECK_STATUS_NORMAL = "NORMAL"
        const val ITEM_CHECK_STATUS_LIMIT_DATE_REACHED = "LIMIT_DATE_REACHED"
        const val ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED = "PROJECTED_DATE_REACHED"
        const val ITEM_CHECK_STATUS_MEASURE_ALERT = "MEASURE_ALERT"
        const val ITEM_CHECK_STATUS_MANUAL_ALERT = "MANUAL_ALERT"
        //
        const val APPLY_MATERIAL_NO = "NO"
        const val APPLY_MATERIAL_OPTIONAL = "OPTIONAL"
        const val APPLY_MATERIAL_REQUIRED = "REQUIRED"

        const val EXEC_TYPE_FIXED = "FIXED"
        const val EXEC_TYPE_ALERT = "ALERT"
        const val EXEC_TYPE_ALREADY_OK = "ALREADY_OK"
        const val EXEC_TYPE_NOT_VERIFIED = "NOT_VERIFIED"
    }

}