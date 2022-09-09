package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    @SerializedName("service_code") var service_code:Int,
    @Expose
    @SerializedName("service_seq") var service_seq:Int,
    var service_desc:String,
    var service_desc_full:String,
    @Expose
    @SerializedName("price")  var price:Double?,
    var manual_price:Int,
    @Expose
    @SerializedName("qty") var qty:Int,
    @Expose
    @SerializedName("comments") var comments:String?
) {
}