package com.namoadigital.prj001.model.next_os

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class SetReservedUserRec(
    @SerializedName("so_return") val so_return: List<SoReturn>
) : Main_Header_Rec() {

    data class SoReturn(
        @SerializedName("customer_code") val customer_code: Int,
        @SerializedName("so_prefix") val so_prefix: Int,
        @SerializedName("so_code") val so_code: Int,
        @SerializedName("so_scn") val so_scn: Int,
        @SerializedName("so_update") val so_update: Int,
        @SerializedName("so_status") val so_status: String?,
        @SerializedName("ret_status") val ret_status: String?,
        @SerializedName("ret_msg") val ret_msg: String?,
        @SerializedName("reserved_user") val reserved_user: Int?,
        @SerializedName("reserved_user_name") val reserved_user_name: String?,
        @SerializedName("reserved_user_nick") val reserved_user_nick: String?,
        @SerializedName("reserved_date") val reserved_date: String?,
    )

}