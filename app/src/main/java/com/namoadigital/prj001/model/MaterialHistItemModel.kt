package com.namoadigital.prj001.model

import java.io.Serializable

class MaterialHistItemModel(
    val materialDesc: String,
    val materialQty: Float,
    val materialUn: String,
    val materialAction: Int,
): Serializable {
    fun formatMaterialHistItem():String{
        return """$materialDesc: $materialQty $materialUn"""
    }
}