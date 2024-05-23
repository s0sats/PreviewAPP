package com.namoadigital.prj001.model.trip.fcm

import com.google.gson.annotations.SerializedName

data class FSTripFCM(
    @SerializedName("trip")val trip:FSTripLongMsg,
){

    data class FSTripLongMsg(
        @SerializedName("trip_prefix")val tripPrefix:Int,
        @SerializedName("trip_code")val tripCode:Int,
        @SerializedName("scn")val scn:Int,
        @SerializedName("status")val status:String
    )


}