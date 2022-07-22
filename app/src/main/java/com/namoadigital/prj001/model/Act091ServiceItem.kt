package com.namoadigital.prj001.model

data class Act091ServiceItem(
    val name: String? = "",
    val price: Double? = 0.0,
    val qty: Int? = 0,
)

fun TSO_Service_Search_Obj.toServiceItem() = Act091ServiceItem(
    name = this.pack_service_desc_full,
    price = this.price,
    qty = this.qty
)
