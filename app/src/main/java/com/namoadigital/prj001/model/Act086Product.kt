package com.namoadigital.prj001.model

import java.io.Serializable

class Act086ProductItem(
    productCode: Int,
    productId: String,
    productDesc: String,
    productUnit: String
):Serializable{
    var productCode: Int = -1
    private set
    var productId: String = ""
        private set
    var productDesc: String = ""
        private set
    var productUnit: String = ""
        private set
    var productQty: Int = 0

    init {
        this.productCode = productCode
        this.productId = productId
        this.productDesc = productDesc
        this.productUnit = productUnit
    }

    fun getFormttedQty() = if(productQty > 0) "$productQty $productUnit" else ""
}