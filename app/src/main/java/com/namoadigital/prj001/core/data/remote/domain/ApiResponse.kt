package com.namoadigital.prj001.core.data.remote.domain

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class ApiResponse<DATA>(
    @SerializedName("data") var data: DATA? = null,
    @SerializedName("status") var status: ApiStatus? = null
) : Main_Header_Rec() {

    data class ApiCollection<DATA>(
        @SerializedName("pagination") var pagination: Pagination? = null,
        @SerializedName("list") var list: List<DATA?> = emptyList()
    ) {

        data class Pagination(
            @SerializedName("lineCount") var lineCount: Int? = null,
            @SerializedName("pageNumber") var pageNumber: Int? = null,
            @SerializedName("pageSize") var pageSize: Int? = null
        )

    }
}
