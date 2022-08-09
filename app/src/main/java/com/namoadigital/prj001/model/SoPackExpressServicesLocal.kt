package com.namoadigital.prj001.model

data class SoPackExpressServicesLocal(
    var customer_code:Long,
    var site_code:Long,
    var operation_code:Long,
    var product_code:Long,
    var express_code:String,
    var express_tmp:Long,
    var pack_code:Int,
    var pack_seq:Int,
    var service_code:Int,
    var service_seq:Int,
    var service_desc:String,
    var service_desc_full:String,
    var price:Double?,
    var manual_price:Int,
    var qty:Int,
    var comments:String?
) {
}