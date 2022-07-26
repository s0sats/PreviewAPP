package com.namoadigital.prj001.model

data class Act091ServiceItem(
    val type_ps: String = "",
    val name: String = "",
    var price: Double? = 0.0,
    val qty: Int = 0,
    val manual_price: Int = 0,
    var serviceList: List<TSO_Service_Search_Detail_Obj>
)

fun TSO_Service_Search_Obj.toServiceItem() = Act091ServiceItem(
    name = this.pack_service_desc_full,
    price = this.price,
    qty = this.qty,
    type_ps = this.type_ps,
    manual_price = this.manual_price,
    serviceList = this.service_list
)

