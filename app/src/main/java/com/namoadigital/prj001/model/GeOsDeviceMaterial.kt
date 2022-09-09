package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GeOsDeviceMaterial(
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("custom_form_type") val custom_form_type: Int,
    @SerializedName("custom_form_code") val custom_form_code: Int,
    @SerializedName("custom_form_version") val custom_form_version: Int,
    @SerializedName("custom_form_data") val custom_form_data: Int,
    @SerializedName("product_code") val product_code: Int,
    @SerializedName("serial_code") val serial_code: Int,
    @SerializedName("device_tp_code") val device_tp_code: Int,
    @SerializedName("item_check_code") val item_check_code: Int,
    @SerializedName("item_check_seq") val item_check_seq: Int,
    @Expose
    @SerializedName("material_code") val material_code: Int,
    @SerializedName("material_id") val material_id: String,
    @SerializedName("material_desc") val material_desc: String,
    @Expose
    @SerializedName("material_qty") var material_qty: Float,
    @SerializedName("material_unit") val material_unit: String?,
    @SerializedName("creation_ms") val creation_ms: Long,
    @Expose
    @SerializedName("material_planned") val material_planned: Int = 0,
    @Expose
    @SerializedName("material_planned_used") var material_planned_used: Int = 0,
    @Expose
    @SerializedName("material_planned_qty") val material_planned_qty: Float? = null
    val origin: String? = null


): Serializable

/**
 * Extension que convert modelo do banco para modelo de u.i
 */
fun GeOsDeviceMaterial.toUiMaterialItem() : Act086MaterialItem{
    return Act086MaterialItem(
        productCode = material_code,
        productId = material_id,
        productDesc = material_desc,
        productUnit = material_unit?:"",
        productQty = material_qty,
        creationMs = creation_ms,
        materialPlanned = material_planned,
        materialPlannedUsed = material_planned_used,
        materialPlannedQty = material_planned_qty,
        origin = origin
    )
}