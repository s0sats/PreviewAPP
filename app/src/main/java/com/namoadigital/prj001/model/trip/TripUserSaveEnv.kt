package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TripUserSaveEnv(
    @Expose @SerializedName("tripPrefix") var tripPrefix: Int,
    @Expose @SerializedName("tripCode") var tripCode: Int,
    @Expose @SerializedName("scn") var scn: Int,
    @Expose @SerializedName("userCode") var userCode: Int,
    @Expose @SerializedName("userSeq") var userSeq: Int? = null,
    @Expose @SerializedName("action") var action: String,
    @Expose @SerializedName("inDate") var inDate: String,
    @Expose @SerializedName("outDate") var outDate: String,
    var userName: String = "",
    var userNick: String = "",
) {

    companion object {

        val WS_BUNDLE_KEY = TripUserSaveEnv::class.java.name

    }

}
