package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GeOsDeviceItem(
    @Expose
    @SerializedName("customer_code")
    val customer_code :Long,
    @Expose
    @SerializedName("custom_form_type")
    val custom_form_type :Int,
    @Expose
    @SerializedName("custom_form_code")
    val custom_form_code :Int,
    @Expose
    @SerializedName("custom_form_version")
    val custom_form_version :Int,
    @Expose
    @SerializedName("custom_form_data")
    val custom_form_data :Int,
    @SerializedName("product_code")
    val product_code :Int,
    @SerializedName("serial_code")
    val serial_code :Int,
    @Expose
    @SerializedName("device_tp_code")
    val device_tp_code :Int,
    @Expose
    @SerializedName("item_check_code")
    val item_check_code :Int,
    @Expose
    @SerializedName("item_check_seq")
    val item_check_seq :Int,
    @SerializedName("item_check_id")
    val item_check_id :String,
    @SerializedName("item_check_desc")
    val item_check_desc :String,
    @SerializedName("item_check_group_code")
    var item_check_group_code: Int?,
    @SerializedName("apply_material")
    val apply_material :String,
    @SerializedName("verification_instruction")
    val verification_instruction :String?,
    @SerializedName("require_justify_problem")
    val require_justify_problem :Int,
    @SerializedName("critical_item")
    var critical_item :Int,//Pode ser modificado pela segunda varredura
    @SerializedName("change_adjust")
    val change_adjust: Int,
    @SerializedName("order_seq")
    val order_seq :Int,
    @Expose
    @SerializedName("structure")
    val structure :Int,
    @Expose
    @SerializedName("manual_desc")
    var manual_desc :String?,
    @SerializedName("next_cycle_measure")
    val next_cycle_measure :Float?,
    @SerializedName("next_cycle_measure_date")
    val next_cycle_measure_date :String?,
    @SerializedName("next_cycle_limit_date")
    val next_cycle_limit_date :String?,
    @SerializedName("value_sufix")
    val value_sufix :String?,
    @SerializedName("restriction_decimal")
    val restriction_decimal :Int?,
    @SerializedName("item_check_status")
    var item_check_status :String,
    @SerializedName("target_date")
    var target_date :String?,
    @Expose
    @SerializedName("exec_type")
    var exec_type :String?,
    //Pediram para remover esse campo, mas desconfio que vai voltar, então fica no banco só não
    //"expoe" no json de envio
    @SerializedName("exec_date")
    var exec_date :String?,
    @Expose
    @SerializedName("exec_comment")
    var exec_comment :String?,
    @Expose
    @SerializedName("exec_photo1")
    var exec_photo1 :String?,
    @Expose
    @SerializedName("exec_photo2")
    var exec_photo2 :String?,
    @Expose
    @SerializedName("exec_photo3")
    var exec_photo3 :String?,
    @Expose
    @SerializedName("exec_photo4") var exec_photo4 :String?,
    @SerializedName("status_answer") var status_answer :String?,
    @SerializedName("has_expired_cycle") var has_expired_cycle :Int,
    @SerializedName("hide_days_in_alert") var hide_days_in_alert :Int,
    @Expose
    @SerializedName("material")
    val materialList: MutableList<GeOsDeviceMaterial>  = mutableListOf(),
    @SerializedName("partitioned_execution") var partitioned_execution: Int,
    @SerializedName("ticket_prefix") var ticket_prefix: Int?,
    @SerializedName("ticket_code") var ticket_code: Int?,
):Serializable {
    fun getGeOsDeviceItemCodeAndSeq(): String{
        return "${item_check_code}.${item_check_seq}"
    }

    val hideDaysInAlert = hide_days_in_alert == 1
    val isCycleExpired = has_expired_cycle == 1
    val isCritical = critical_item == 1
    val isNO_CYCLE = next_cycle_measure == null && next_cycle_limit_date == null

    companion object{
        const val ITEM_CHECK_STATUS_NO_CYCLE = "NO_CYCLE"
        const val ITEM_CHECK_STATUS_NORMAL = "NORMAL"
        const val ITEM_CHECK_STATUS_LIMIT_DATE_REACHED = "LIMIT_DATE_REACHED"
        const val ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED = "PROJECTED_DATE_REACHED"
        const val ITEM_CHECK_STATUS_MEASURE_ALERT = "MEASURE_ALERT"
        const val ITEM_CHECK_STATUS_MANUAL_ALERT = "MANUAL_ALERT"
        const val ITEM_CHECK_STATUS_MANUAL = "MANUAL"
        const val ITEM_CHECK_STATUS_FORCED = "STATUS_FORCED"
        //
        const val APPLY_MATERIAL_NO = "NO"
        const val APPLY_MATERIAL_OPTIONAL = "OPTIONAL"
        const val APPLY_MATERIAL_REQUIRED = "REQUIRED"

        const val EXEC_TYPE_FIXED = "FIXED"
        const val EXEC_TYPE_ADJUST = "ADJUST"
        const val EXEC_TYPE_ALERT = "ALERT"
        const val EXEC_TYPE_ALREADY_OK = "ALREADY_OK"
        const val EXEC_TYPE_NOT_VERIFIED = "NOT_VERIFIED"
    }

}