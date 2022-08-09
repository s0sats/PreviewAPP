package com.namoadigital.prj001.model

import android.content.Context
import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao
import com.namoadigital.prj001.sql.SoPackExpressPacksLocalSql003
import com.namoadigital.prj001.sql.SoPackExpressServicesLocalSql003
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con

data class SOExpressItemHeader(
    var customer_code:Long,
    val pack_code:Int,
    var site_code:Long,
    var operation_code:Long,
    var product_code:Long,
    var express_code:String,
    val price_list_code:Int,
    val type_ps: String = "",
    var packSeq: Int = -1,
    val name: String = "",
    val pack_service_desc: String = "",
    var comment: String = "",
    var price: Double? = 0.0,
    var qty: Int = 0,
    val manual_price: Int = 0,
    var serviceList: MutableList<SOExpressItemDetail> = mutableListOf()
) {
    fun toSoPackExpressPacksLocal(
        context: Context,
        so_pack_express_local: SO_Pack_Express_Local,
        site_code: Long,
        operation_code: Long,
        product_code: Long,
        express_code: String
    ): SoPackExpressPacksLocal? {

//        if(packSeq < 0) {
//            packSeq = generatePackSeq(
//                context,
//                customer_code,
//                site_code,
//                operation_code,
//                product_code,
//                express_code,
//                so_pack_express_local.express_tmp,
//                pack_code
//            )
//        }
//        var soPackExpressPacksLocal = SoPackExpressPacksLocal(
//            pack_code,
//            packSeq,
//            price_list_code,
//            name,
//            name,
//            manual_price,
//            price?:0.0,
//            qty,
//            type_ps,
//            comment
//        )
//        soPackExpressPacksLocal.setPkAndServiceList(context, so_pack_express_local, serviceList)
//        return soPackExpressPacksLocal
        return null
    }


}



fun List<TSO_Service_Search_Detail_Obj>.toSOExpressItemDetail() = run {
    val list = mutableListOf<SOExpressItemDetail>()
    this.forEach {
        list.add(
            SOExpressItemDetail(
            pack_code = it.pack_code,
            service_code = it.service_code,
            service_desc = it.service_desc,
            service_desc_full = it.service_desc_full,
            price = it.price,
            manual_price = it.manual_price,
            qty = it.qty,
            comments = it.comment)
        )
    }
    list
}

@JvmName("toSOExpressItemDetailSoPackExpressServicesLocal")
fun MutableList<SoPackExpressServicesLocal>.toSOExpressItemDetail() = run {
    val list = mutableListOf<SOExpressItemDetail>()
    this.forEach {
        list.add(
            SOExpressItemDetail(
            pack_code = it.pack_code,
            service_code = it.service_code,
            service_desc = it.service_desc,
            service_desc_full = it.service_desc_full,
            price = it.price,
            manual_price = it.manual_price,
            qty = it.qty,
            comments = it.comments)
        )
    }
    list
}

