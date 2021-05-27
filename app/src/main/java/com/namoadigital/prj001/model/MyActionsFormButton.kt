package com.namoadigital.prj001.model
data class MyActionsFormButton(
        val productCode: Int,
        val productDesc: String,
        val serialId: String,
        val label: String,
        val orderBy:String  = "999912312359"
):MyActionsBase()
