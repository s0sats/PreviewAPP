package com.namoadigital.prj001.model.next_os

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class ListReservedUserRec(
    @SerializedName("result") var result: ResultRec? = null
) : Main_Header_Rec() {

    data class ResultRec(
        @SerializedName("users") var users: List<Users>? = null
    ){

        data class Users(
            @SerializedName("userCode") var userCode: Int? = null,
            @SerializedName("userName") var userName: String? = null,
            @SerializedName("userNick") var userNick: String? = null,
        )

    }

}