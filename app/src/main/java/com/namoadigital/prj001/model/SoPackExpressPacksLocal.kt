package com.namoadigital.prj001.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao
import com.namoadigital.prj001.sql.SoPackExpressPacksLocalSql003
import com.namoadigital.prj001.sql.SoPackExpressServicesLocalSql003
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import java.util.ArrayList

data class SoPackExpressPacksLocal(
    var customer_code:Long,
    var site_code:Long,
    var operation_code:Long,
    var product_code:Long,
    var express_code:String,
    var express_tmp:Long,
    val pack_code:Int,
    var pack_seq:Int,
    val price_list_code:Int,
    val pack_service_desc:String,
    val pack_service_desc_full:String,
    val manual_price: Int = 0,
    var price:Double? =0.0,
    var qty:Int,
    val type_ps:String,
    var comments:String?,
    @SerializedName("service")
    val serviceList: MutableList<SoPackExpressServicesLocal> = mutableListOf<SoPackExpressServicesLocal>()
) {
    constructor(
        context: Context,
        serviceSearch: TSO_Service_Search_Obj,
        expressLocal:SO_Pack_Express_Local,
        pack_seq: Int) :
            this(
                expressLocal.customer_code,
                expressLocal.site_code,
                expressLocal.operation_code,
                expressLocal.product_code,
                expressLocal.express_code,
                expressLocal.express_tmp,
                serviceSearch.pack_code,
                pack_seq,
                serviceSearch.price_list_code,
                serviceSearch.pack_service_desc,
                serviceSearch.pack_service_desc_full,
                serviceSearch.manual_price,
                serviceSearch.price,
                if(serviceSearch.qty==0){1}else{serviceSearch.qty},
                serviceSearch.type_ps,
                serviceSearch.comment
            ){
        setPkAndServiceList(context, expressLocal, serviceSearch.service_list)
            }


    fun setPkAndServiceList(
        context: Context,
        soExpress: SO_Pack_Express_Local,
        inputServiceList: List<TSO_Service_Search_Detail_Obj>
    ){
        this.customer_code = soExpress.customer_code
        this.site_code = soExpress.site_code
        this.operation_code = soExpress.operation_code
        this.product_code = soExpress.product_code
        this.express_code = soExpress.express_code
        this.express_tmp = soExpress.express_tmp
        var tmpServiceSeq = getServiceSeq(
            context,
            this.customer_code,
            this.site_code,
            this.operation_code,
            this.product_code,
            this.express_code,
            this.express_tmp,
            this.pack_code,
            this.pack_seq
        )
        inputServiceList.forEach {
            var service = SoPackExpressServicesLocal(
                soExpress.customer_code,
                        soExpress.site_code,
                        soExpress.operation_code,
                        soExpress.product_code,
                        soExpress.express_code,
                        soExpress.express_tmp,
                        pack_code,
                pack_seq,
                it.service_code,
                tmpServiceSeq,
                it.service_desc,
                it.service_desc_full,
                it.price?:0.0,
                it.manual_price,
                it.qty,
                it.comment
            )

            serviceList.add(
                service
            )
            tmpServiceSeq++
        }
    }

    private fun getServiceSeq(
        context: Context,
        customerCode: Long,
        siteCode: Long,
        operationCode: Long,
        productCode: Long,
        expressCode: String,
        expressTmp: Long,
        packCode: Int,
        packSeq: Int
    ): Int {
        return SoPackExpressPacksLocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        ).getByStringHM(
            SoPackExpressServicesLocalSql003(
                customerCode,
                siteCode,
                operationCode,
                productCode,
                expressCode,
                expressTmp,
                packCode,
                packSeq
            ).toSqlQuery()
        )?.get(SoPackExpressServicesLocalSql003.NEXT_TMP)?.toInt()!!
    }

    fun toSOExpressItemHeader(): SOExpressItemHeader {
        return SOExpressItemHeader(
            customer_code = this.customer_code,
            pack_code = this.pack_code,
            site_code = this.site_code,
            operation_code = this.operation_code,
            product_code = this.product_code,
            express_code = this.express_code,
            packSeq = this.pack_seq,
            price_list_code = this.price_list_code,
            name = this.pack_service_desc_full,
            pack_service_desc = this.pack_service_desc,
            comment = this.comments?: "",
            price = this.price,
            qty = this.qty,
            type_ps = this.type_ps,
            manual_price = this.manual_price,
            serviceList = this.serviceList.toSOExpressItemDetail()
        )
    }
}