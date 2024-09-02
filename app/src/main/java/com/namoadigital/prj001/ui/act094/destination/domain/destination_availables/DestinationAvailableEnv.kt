package com.namoadigital.prj001.ui.act094.destination.domain.destination_availables

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env

data class DestinationAvailableEnv(
    @Expose @SerializedName("pageSize") var pageSize: Int = 9999,
    @Expose @SerializedName("pageNumber") var pageNumber: Int = 1,
    @Expose @SerializedName("parameters") var parameters: Parameters = Parameters()
) : Main_Header_Env() {

    data class Parameters(
        @Expose @SerializedName("showOnlyToday") var showOnlyToday: Int = 0,
        @Expose @SerializedName("showOnlyPriority") var showOnlyPriority: Int = 0,
        @Expose @SerializedName("hidePreventive") var hidePreventive: Int = 0,
        @Expose @SerializedName("showOnlySiteWithPlanning") var showOnlySiteWithPlanning: Int = 0,
        @Expose @SerializedName("siteCode") var siteCode: Int? = null
    )

}
