package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import java.io.Serializable

class GeOsDeviceMaterial(
    val customer_code: Long,
    val custom_form_type: Int,
    val custom_form_code: Int,
    val custom_form_version: Int,
    val custom_form_data: Int,
    val product_code: Int,
    val serial_code: Int,
    val device_tp_code: Int,
    val item_check_code: Int,
    val item_check_seq: Int,
    @Expose
    val material_code: Int,
    val material_id: String,
    val material_desc: String,
    @Expose
    var material_qty: Float,
    val material_unit: String?,
    val creation_ms: Long,
    @Expose
    val material_planned: Int = 0,
    @Expose
    var material_planned_used: Int = 0,
    @Expose
    val material_planned_qty: Float? = null

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
        materialPlannedQty = material_planned_qty
    )
}