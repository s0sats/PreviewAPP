package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class TripUserSaveEnv(
    @SerializedName("tripPrefix") var tripPrefix: Int,
    @SerializedName("tripCode") var tripCode: Int,
    @SerializedName("scn") var scn: Int,
    @SerializedName("userCode") var userCode: Int,
    @SerializedName("userSeq") var userSeq: Int? = null,
    @SerializedName("action") var action: String,
    @SerializedName("inDate") var inDate: String,
    @SerializedName("outDate") var outDate: String,
    var userName: String = "",
    var userNick: String = "",
) {

    companion object {

        val WS_BUNDLE_KEY = TripUserSaveEnv::class.java.name

    }

}
