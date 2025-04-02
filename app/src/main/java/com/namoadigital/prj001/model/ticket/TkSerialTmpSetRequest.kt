package com.namoadigital.prj001.model.ticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TkSerialTmpSetRequest(
    @Expose
    @SerializedName("ticketPrefix") val prefix: Int,
    @Expose
    @SerializedName("ticketCode") val code: Int,
    @Expose
    @SerializedName("productCode") val productCode: Int,
    @Expose
    @SerializedName("serialCode") val serialCode: Int
) {
    companion object{
        val WS_BUNDLE_KEY = TkSerialTmpSetRequest::class.java.name
    }
}
