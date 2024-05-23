package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FSTripUser(
    @SerializedName("userCode") val userCode: Int,
    @SerializedName("userSeq") val userSeq: Int,
    @SerializedName("userName") val userName: String,
    @SerializedName("inDate") val dateStart: String,
    @SerializedName("outDate") val dateEnd: String?,
) {

    @SerializedName("customerCode")
    var customerCode: Long = -1
    @SerializedName("tripPrefix")
    var tripPrefix: Int = -1
    @SerializedName("tripCode")
    var tripCode: Int = -1


    constructor(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        userCode: Int,
        userSeq: Int,
        userName: String,
        dateStart: String,
        dateEnd: String?,
    ) : this(
        userCode,
        userSeq,
        userName,
        dateStart,
        dateEnd,
    ) {
        this.customerCode = customerCode
        this.tripPrefix = tripPrefix
        this.tripCode = tripCode
    }

    fun setPk(fsTrip: FSTrip) {
        this.customerCode = fsTrip.customerCode
        this.tripPrefix = fsTrip.tripPrefix
        this.tripCode = fsTrip.tripCode
    }
}