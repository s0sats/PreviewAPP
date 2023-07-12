package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SO_Status_Change_Return (
@Expose @SerializedName("customer_code") val customer_code: Int ,
@Expose @SerializedName("so_prefix") val so_prefix: Int ,
@Expose @SerializedName("so_code") val so_code: Int ,
@Expose @SerializedName("so_scn") val so_scn: Int ,
@Expose @SerializedName("so_update") val so_update: Int,
@Expose @SerializedName("so_status") val so_status: String,
@Expose @SerializedName("ret_status") val ret_status: String?,
@Expose @SerializedName("ret_msg") val ret_msg: String?
)
