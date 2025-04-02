package com.namoadigital.prj001.model.ticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.service.ticket.WsSerialSearchList

data class TkSerialSearchRequest(
    @Expose
    @SerializedName("pageNumber") var pageNumber: Int? = DEFAULT_PAGE_NUMBER,
    @Expose
    @SerializedName("pageSize") var pageSize: Int? = DEFAULT_PAGE_SIZE,
    @Expose
    @SerializedName("serialId") var serialId: String? = null,
    @Expose
    @SerializedName("productCode") var productCode: Int? = null
) {
    //Criado para uso em Java
    constructor(serialId: String?): this(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, serialId, null)

    companion object {
        val WS_BUNDLE_KEY = TkSerialSearchRequest::class.java.name
        const val DEFAULT_PAGE_NUMBER: Int = 1
        const val DEFAULT_PAGE_SIZE: Int = 100
    }
}