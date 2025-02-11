package com.namoadigital.prj001.core.data.domain.model

enum class BarCodeTypeFlow {
    CAM,
    DEFAULT
}

data class BarCodeFlow(
    var flowBarcode: BarCodeTypeFlow = BarCodeTypeFlow.DEFAULT
){
    companion object {
        fun toType(value: String) = BarCodeTypeFlow.valueOf(value)
    }
}