package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScheduleNotExecutedRec(
    @SerializedName("app")
    @Expose
    val app:String,
    @SerializedName("validation")
    @Expose
    val validation:String,
    @SerializedName("link_url")
    @Expose
    val link_url:String,
    @SerializedName("error_msg")
    @Expose
    val error_msg:String,
    @SerializedName("save")
    @Expose
    val save: String
)
