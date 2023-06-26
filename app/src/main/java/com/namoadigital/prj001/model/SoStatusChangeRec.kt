package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SoStatusChangeRec(
    @Expose @SerializedName("app")
    var app: String,

    @Expose
    @SerializedName("validation")
    val validation: String,

    @Expose
    @SerializedName("link_url")
    val link_url: String,

    @Expose
    @SerializedName("error_msg")
    val error_msg: String,

    @Expose
    @SerializedName("so_status")
    val so_status: ArrayList<SO_Status_Change_Return>,

    @Expose
    @SerializedName("so")
    val so: TSO_SO_Service_List?,
)