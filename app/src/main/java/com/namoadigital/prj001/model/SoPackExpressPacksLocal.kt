package com.namoadigital.prj001.model

class SoPackExpressPacksLocal(
    val pack_code:Int,
    val pack_seq:Int,
    val price_list_code:Int,
    var pack_service_desc:String,
    var pack_service_desc_full:String,
    var qty:Int,
    val type_ps:String?,
    var comments:String?,
    val serviceList: MutableList<SoPackExpressServicesLocal> = mutableListOf<SoPackExpressServicesLocal>()
) {
    var customer_code:Long = -1
    var site_code:Long = -1
    var operation_code:Long = -1
    var product_code:Long = -1
    var express_code:String = ""
    var express_tmp:Long = -1

    constructor(
        customer_code:Long,
        site_code:Long,
        operation_code:Long,
        product_code:Long,
        express_code:String,
        express_tmp:Long,
        pack_code:Int,
        pack_seq:Int,
        price_list_code:Int,
        pack_service_desc:String,
        pack_service_desc_full:String,
        qty:Int,
        type_ps:String?,
        comments:String?
    ) : this(
        pack_code,
        pack_seq,
        price_list_code,
        pack_service_desc,
        pack_service_desc_full,
        qty,
        type_ps,
        comments
    ) {
        this.customer_code = customer_code
        this.site_code = site_code
        this.operation_code = operation_code
        this.product_code = product_code
        this.express_code = express_code
        this.express_tmp = express_tmp
    }

    fun setPk(soExpress: SO_Pack_Express_Local){
        this.customer_code = soExpress.customer_code
        this.site_code = soExpress.site_code
        this.operation_code = soExpress.operation_code
        this.product_code = soExpress.product_code
        this.express_code = soExpress.express_code
        this.express_tmp = soExpress.express_tmp
        this.serviceList.forEach {
            it.setPk(this)
        }
    }
}