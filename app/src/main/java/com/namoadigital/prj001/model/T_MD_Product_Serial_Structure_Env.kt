package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class T_MD_Product_Serial_Structure_Env(
    @SerializedName("customer_code")
    val customerCode: Long,
    @SerializedName("product_code")
    val productCode: Long,
    @SerializedName("serial_code")
    val serialCode: Long,
    @SerializedName("scn_item_check")
    val scnItemCheck: Int
): Serializable