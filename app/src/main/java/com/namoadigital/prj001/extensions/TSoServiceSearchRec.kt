package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.model.TSO_Service_Search_Rec

fun TSO_Service_Search_Rec.getPackageDefault(
    type_ps: String,
    customer_code: Long,
    price_list_code: Int,
    pack_code: Int,
    pack_service_desc: String
): TSO_Service_Search_Obj{
    return this.data.filter{
        it.type_ps == type_ps
                && it.customer_code == customer_code
                && it.price_list_code == price_list_code
                && it.pack_code == pack_code
                && it.pack_service_desc == pack_service_desc
    }[0]

}