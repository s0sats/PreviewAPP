package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class AvailableUsersRec(
    @SerializedName("userCode") var userCode: Int?,
    @SerializedName("userName") var userName: String?,
    @SerializedName("userNick") var userNick: String?
)