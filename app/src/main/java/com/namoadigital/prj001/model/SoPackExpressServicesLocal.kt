package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose

data class SoPackExpressServicesLocal(
    var customer_code:Long,
    var site_code:Long,
    var operation_code:Long,
    var product_code:Long,
    var express_code:String,
    var express_tmp:Long,
    var price_list_code:Int,
    var pack_code:Int,
    var pack_seq:Int,
    var type_ps:String,
    @Expose
    var service_code:Int,
    @Expose
    var service_seq:Int,
    var service_desc:String,
    var service_desc_full:String,
    @Expose
    var price:Double?,
    var manual_price:Int,
    @Expose
    var qty:Int,
    @Expose
    var comments:String?
) {
}