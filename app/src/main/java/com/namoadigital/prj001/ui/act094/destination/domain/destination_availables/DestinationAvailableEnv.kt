package com.namoadigital.prj001.ui.act094.destination.domain.destination_availables

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env

data class DestinationAvailableEnv(
    @SerializedName("pageSize") var pageSize: Int = 9999,
    @SerializedName("pageNumber") var pageNumber: Int = 1,
    @SerializedName("parameters") var parameters: Parameters = Parameters()
) : Main_Header_Env() {

    data class Parameters(
        @SerializedName("showOnlyToday") var showOnlyToday: Int = 0,
        @SerializedName("showOnlyPriority") var showOnlyPriority: Int = 0,
        @SerializedName("hidePreventive") var hidePreventive: Int = 0,
        @SerializedName("showOnlySiteWithPlanning") var showOnlySiteWithPlanning: Int = 0,
        @SerializedName("siteCode") var siteCode: Int? = null
    )

}
