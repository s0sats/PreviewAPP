package com.namoadigital.prj001.model

import android.content.Context

class SOExpressItemDetail(
    var pack_code:Int,
    var service_code:Int,
    var service_desc:String,
    var service_desc_full:String,
    var price:Double?,
    var manual_price:Int,
    var qty:Int,
    var comments:String?
) {
}