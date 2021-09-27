package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_MD_Product_Serial_Backup_Env   (
    @SerializedName("so_product_code")
    val soProductCode: Long,
    @SerializedName("so_serial_code")
    val soSerialCode: Long,
    @SerializedName("product_code")
    val productCode: Long,
    @SerializedName("serial_id")
    val serialId: String?,
    @SerializedName("site_code")
    val siteCode: Int
): Main_Header_Env()