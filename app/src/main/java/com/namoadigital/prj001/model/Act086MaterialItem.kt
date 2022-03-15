package com.namoadigital.prj001.model

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.Serializable

class Act086MaterialItem(
    productCode: Int,
    productId: String,
    productDesc: String,
    productUnit: String,
    productQty: Float = 0f,
    creationMs: Long = 0,
    materialPlanned: Int = 0,
    materialPlannedUsed: Int = 0,
    materialPlannedQty: Float? = null
):Serializable{
    var productCode: Int = -1
    private set
    var productId: String = ""
        private set
    var productDesc: String = ""
        private set
    var productUnit: String = ""
        private set
    var productQty: Float = 0f

    var creationMs: Long = 0
        private set
    var materialPlanned: Int = 0
        private set
    var materialPlannedUsed: Int = 0
    var materialPlannedQty: Float? = null
        private set

    init {
        this.productCode = productCode
        this.productId = productId
        this.productDesc = productDesc
        this.productUnit = productUnit
        this.productQty = productQty
        this.creationMs = creationMs
        this.materialPlanned = materialPlanned
        this.materialPlannedUsed = materialPlannedUsed
        this.materialPlannedQty = materialPlannedQty
    }

    fun getFormttedQty(lbl: String) = if(productQty > 0f) "$lbl: ${ToolBox_Inf.convertFloatToBigDecimalString(productQty,4,true)} $productUnit" else ""
    fun getFormttedPlannedQty(lbl: String) = if(materialPlannedQty != null && materialPlannedQty!! > 0f) "$lbl: ${ToolBox_Inf.convertFloatToBigDecimalString(materialPlannedQty!!,4,true)} $productUnit" else ""
    fun getFormattedMaterialDesc(): SpannableString {
        val s = "$productDesc ($productId)"
        val span = SpannableString(s)
        span.setSpan(
            RelativeSizeSpan(
                0.8f
            ),
            s.indexOf("($productId"),
            s.length,
            0
        )
        return span
    }
}