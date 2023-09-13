package com.namoadigital.prj001.model

class MaterialHistItemModel(
    val materialDesc: String,
    val materialQty: Float,
    val materialUn: String,
    val materialAction: Int,
){
    fun formatMaterialHistItem():String{
        return """$materialDesc: $materialQty $materialUn"""
    }
}