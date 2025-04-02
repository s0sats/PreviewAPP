package com.namoadigital.prj001.model.ticket

import com.google.gson.annotations.SerializedName

data class TkSerialSearchListResponse(
    @SerializedName("productCode") var productCode: Int? = null,
    @SerializedName("productDesc") var productDesc: String? = null,
    @SerializedName("serialCode") var serialCode: Int? = null,
    @SerializedName("serialId") var serialId: String? = null,
    @SerializedName("siteCode") var siteCode: Int? = null,
    @SerializedName("siteDesc") var siteDesc: String? = null,
    @SerializedName("ticketOpenCnt") var ticketOpenCnt: Int? = null
)