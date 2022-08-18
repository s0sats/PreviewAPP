package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.model.SO_Pack_Express
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj

fun TSO_Service_Search_Detail_Obj.toSoPackExpressServicesLocal(mSo_pack_express: SO_Pack_Express): SoPackExpressServicesLocal{
    return SoPackExpressServicesLocal(
        this.customer_code,
        mSo_pack_express.site_code,
        mSo_pack_express.operation_code,
        mSo_pack_express.product_code,
        mSo_pack_express.express_code,
        -1,
        mSo_pack_express.price_list_code,
        this.pack_code,
        -1,
        "P",
        this.service_code,
        -1,
        this.service_desc,
        this.service_desc_full,
        this.price,
        this.manual_price,
        this.qty,
        this.comment
    )
}