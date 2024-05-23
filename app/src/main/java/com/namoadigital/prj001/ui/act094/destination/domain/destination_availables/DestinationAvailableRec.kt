package com.namoadigital.prj001.ui.act094.destination.domain.destination_availables

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class DestinationAvailableRec(
    @SerializedName("data") var data: Data?
) : Main_Header_Rec() {


    data class Data(
        @SerializedName("pagination") var pagination: Pagination?,
        @SerializedName("list") var list: List<DestinationAvailables?>
    ) {

        data class Pagination(
            @SerializedName("lineCount") var lineCount: Int?,
            @SerializedName("pageNumber") var pageNumber: Int?,
            @SerializedName("pageSize") var pageSize: Int?
        )

    }

}