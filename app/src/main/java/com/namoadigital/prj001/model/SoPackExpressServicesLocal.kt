package com.namoadigital.prj001.model

class SoPackExpressServicesLocal(
    var service_code:Int,
    var service_seq:Int,
    var service_desc:String,
    var service_desc_full:String,
    var price:Double,
    var manual_price:Int,
    var qty:Int,
    var comments:String?
) {

    var customer_code:Long = -1
    var site_code:Long = -1
    var operation_code:Long = -1
    var product_code:Long = -1
    var express_code:String = ""
    var express_tmp:Long = -1
    var pack_code:Int = -1
    var pack_seq:Int = -1


    constructor(
        customer_code:Long,
        site_code:Long,
        operation_code:Long,
        product_code:Long,
        express_code:String,
        express_tmp:Long,
        pack_code:Int,
        pack_seq:Int,
        service_code:Int,
        service_seq:Int,
        service_desc:String,
        service_desc_full:String,
        price:Double,
        manual_price:Int,
        qty:Int,
        comments:String?
    ) : this(
        service_code,
        service_seq,
        service_desc,
        service_desc_full,
        price,
        manual_price,
        qty,
        comments
    ) {
        this.customer_code = customer_code
        this.site_code = site_code
        this.operation_code = operation_code
        this.product_code = product_code
        this.express_code = express_code
        this.express_tmp = express_tmp
        this.pack_code = pack_code
        this.pack_seq = pack_seq
    }

    fun setPk(soPackExpressPacksLocal: SoPackExpressPacksLocal) {
        this.customer_code = soPackExpressPacksLocal.customer_code
        this.site_code = soPackExpressPacksLocal.site_code
        this.operation_code = soPackExpressPacksLocal.operation_code
        this.product_code = soPackExpressPacksLocal.product_code
        this.express_code = soPackExpressPacksLocal.express_code
        this.express_tmp = soPackExpressPacksLocal.express_tmp
        this.pack_code = soPackExpressPacksLocal.pack_code
        this.pack_seq = soPackExpressPacksLocal.pack_seq
    }
}