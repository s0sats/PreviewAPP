package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class T_DataPackage_MD_Product_Serial_Structure_Env(
    @SerializedName("customer_code")
    val customerCode: String,
    @SerializedName("product_code")
    val productCode: String,
    @SerializedName("serial_code")
    val serialCode: String,
    @SerializedName("scn_item_check")
    val scnItemCheck: String
): Serializable