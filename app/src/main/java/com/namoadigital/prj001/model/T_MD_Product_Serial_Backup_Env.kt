package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class T_MD_Product_Serial_Backup_Env   (
    @SerializedName("so_product_code")
    @Expose
    val soProductCode: Long,
    @SerializedName("so_serial_code")
    @Expose
    val soSerialCode: Long,
    @SerializedName("product_code")
    @Expose
    val productCode: Long?,
    @SerializedName("serial_id")
    @Expose
    val serialId: String?,
    @SerializedName("site_code")
    @Expose
    val siteCode: Int
): Main_Header_Env()