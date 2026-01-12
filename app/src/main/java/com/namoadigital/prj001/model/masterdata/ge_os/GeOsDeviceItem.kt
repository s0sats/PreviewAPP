package com.namoadigital.prj001.model.masterdata.ge_os

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GeOsDeviceItem(
    @Expose
    @SerializedName("customer_code")
    val customer_code: Long,
    @Expose
    @SerializedName("custom_form_type")
    val custom_form_type: Int,
    @Expose
    @SerializedName("custom_form_code")
    val custom_form_code: Int,
    @Expose
    @SerializedName("custom_form_version")
    val custom_form_version: Int,
    @Expose
    @SerializedName("custom_form_data")
    val custom_form_data: Int,
    @SerializedName("product_code")
    val product_code: Int,
    @SerializedName("serial_code")
    val serial_code: Int,
    @Expose
    @SerializedName("device_tp_code")
    val device_tp_code: Int,
    @Expose
    @SerializedName("item_check_code")
    val item_check_code: Int,
    @Expose
    @SerializedName("item_check_seq")
    val item_check_seq: Int,
    @SerializedName("item_check_id")
    val item_check_id: String,
    @SerializedName("item_check_desc")
    val item_check_desc: String,
    @SerializedName("item_check_desc_alt_vg")
    val item_check_desc_alt_vg: String?,
    @SerializedName("item_check_group_code")
    var item_check_group_code: Int?,
    @SerializedName("apply_material")
    val apply_material: String,
    @SerializedName("verification_instruction")
    val verification_instruction: String?,
    @SerializedName("require_justify_problem")
    val require_justify_problem: Int,
    @SerializedName("critical_item")
    var critical_item: Int,//Pode ser modificado pela segunda varredura
    @SerializedName("change_adjust")
    val change_adjust: Int,
    @SerializedName("order_seq")
    val order_seq: Int,
    @Expose
    @SerializedName("structure")
    val structure: Int,
    @Expose
    @SerializedName("already_ok_hide")
    val already_ok_hide: Int,
    @Expose
    @SerializedName("require_photo_fixed")
    val require_photo_fixed: Int,
    @Expose
    @SerializedName("require_photo_alert")
    val require_photo_alert: Int,
    @Expose
    @SerializedName("require_photo_already_ok")
    val require_photo_already_ok: Int,
    @Expose
    @SerializedName("require_photo_not_verified")
    val require_photo_not_verified: Int,
    @Expose
    @SerializedName("vg_code")
    val vg_code: Int?,
    @Expose
    @SerializedName("manual_desc")
    var manual_desc: String?,
    @SerializedName("next_cycle_measure")
    val next_cycle_measure: Float?,
    @SerializedName("next_cycle_measure_date")
    val next_cycle_measure_date: String?,
    @SerializedName("next_cycle_limit_date")
    val next_cycle_limit_date: String?,
    @SerializedName("value_sufix")
    val value_sufix: String?,
    @SerializedName("restriction_decimal")
    val restriction_decimal: Int?,
    @SerializedName("item_check_status")
    var item_check_status: String,
    @SerializedName("target_date")
    var target_date: String?,
    @Expose
    @SerializedName("exec_type")
    var exec_type: String?,
    //Pediram para remover esse campo, mas desconfio que vai voltar, então fica no banco só não
    //"expoe" no json de envio
    @SerializedName("exec_date")
    var exec_date: String?,
    @Expose
    @SerializedName("exec_comment")
    var exec_comment: String?,
    @Expose
    @SerializedName("exec_photo1")
    var exec_photo1: String?,
    @Expose
    @SerializedName("exec_photo2")
    var exec_photo2: String?,
    @Expose
    @SerializedName("exec_photo3")
    var exec_photo3: String?,
    @Expose
    @SerializedName("exec_photo4") var exec_photo4: String?,
    @SerializedName("status_answer") var status_answer: String?,
    @SerializedName("has_expired_cycle") var has_expired_cycle: Int,
    @SerializedName("hide_days_in_alert") var hide_days_in_alert: Int,
    @Expose
    @SerializedName("material")
    val materialList: MutableList<GeOsDeviceMaterial> = mutableListOf(),
    @SerializedName("partitioned_execution") var partitioned_execution: Int,
    @SerializedName("ticket_prefix") var ticket_prefix: Int?,
    @SerializedName("ticket_code") var ticket_code: Int?,
    @SerializedName("vg_action") val vg_action: Int,
    @SerializedName("is_visible") var is_visible: Int,
    @SerializedName("color_item") var color_item: GeOsDeviceItemStatusColor?,
    @Expose
    @SerializedName("status_modification_type") var status_modification_type: GeOsDeviceItemStatusModificationType?,
    @SerializedName("automatic_selection_state") var automatic_selection_state: GeOsDeviceItemAutomaticSelectionState?,

    //Measure Item
    @SerializedName("measure_active") val measureActive: Int? = null,
    @SerializedName("measure_require_id") val measureRequireId: Int? = null,
    @Expose @SerializedName("measure_un") val measureUn: String? = null,
    @SerializedName("measure_min") val measureMin: Double? = null,
    @SerializedName("measure_max") val measureMax: Double? = null,
    @SerializedName("measure_alert_min") val measureAlertMin: Double? = null,
    @SerializedName("measure_alert_max") val measureAlertMax: Double? = null,
    @SerializedName("last_measure_value") val lastMeasureValue: Double? = null,
    @SerializedName("last_measure_id") val lastMeasureId: String? = null,
    @SerializedName("last_measure_un") val lastMeasureUn: String? = null,
    @SerializedName("last_measure_date") val lastMeasureDate: String? = null,
    @SerializedName("last_measure_alert") val lastMeasureAlert: Int? = null,
    @Expose
    @SerializedName("measure_ini_value") var measureStartValue: Double? = null,
    @Expose
    @SerializedName("measure_fin_value") var measureEndValue: Double? = null,
    @Expose
    @SerializedName("measure_ini_id") var measureStartId: String? = null,
    @Expose
    @SerializedName("measure_fin_id") var measureEndId: String? = null,
    @SerializedName("label_fixed") var labelFixed: Int,
    @SerializedName("label_already_ok") var labelAlreadyOk: Int,
) : Serializable {


    val hasMeasureActive = measureActive != null && measureActive == 1
    val isRequiredID = measureRequireId != null && measureRequireId == 1
    val isMeasureAlert = lastMeasureAlert != null && lastMeasureAlert == 1
    val containLastDate = lastMeasureDate != null

    val ticketFormatted = "${ticket_prefix}.${ticket_code}"

    fun getGeOsDeviceItemCodeAndSeq(): String {
        return "${item_check_code}.${item_check_seq}"
    }

    val hideDaysInAlert = hide_days_in_alert == 1
    val isCycleExpired = has_expired_cycle == 1
    val isCritical = critical_item == 1
    val isNO_CYCLE = next_cycle_measure == null && next_cycle_limit_date == null
    val bypassVGVisibility = !isNO_CYCLE && color_item == GeOsDeviceItemStatusColor.YELLOW


    companion object {
        const val ITEM_CHECK_STATUS_NO_CYCLE = "NO_CYCLE" //NO_CYCLE
        const val ITEM_CHECK_STATUS_NORMAL = "NORMAL" //NORMAL
        const val ITEM_CHECK_STATUS_LIMIT_DATE_REACHED =
            "LIMIT_DATE_REACHED" //LIMIT_DATE_REACHED -> VOLTAR PARA NORMAL
        const val ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED =
            "PROJECTED_DATE_REACHED" //PROJECTED_DATE_REACHED -> VOLTAR PARA NORMAL
        const val ITEM_CHECK_STATUS_MEASURE_ALERT =
            "MEASURE_ALERT" //MEASURE_ALERT -> VOLTAR PARA NORMAL
        const val ITEM_CHECK_STATUS_MANUAL_ALERT = "MANUAL_ALERT" //MANUAL_ALERT
        const val ITEM_CHECK_STATUS_MANUAL = "MANUAL" //
        const val ITEM_CHECK_STATUS_FORCED = "STATUS_FORCED" //
        const val ITEM_CHECK_STATUS_MANUALLY_FORCED_ITEM =
            "MANUALLY_FORCED_ITEM" // MANUALLY_FORCED_ITEM
        const val ITEM_CHECK_STATUS_MANUALLY_FORCED_DATE =
            "MANUALLY_FORCED_DATE" // MANUALLY_FORCED_DATE   -> VOLTAR PARA NORMAL

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