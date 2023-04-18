package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class ScheduleNotExecutedEnv(
    app_code: String,
    app_version: String,
    app_type: String,
    session_app: String,
    @SerializedName("token") val token: String,
    @SerializedName("customer_code") val customer_code: Long,
    @SerializedName("schedule_prefix") val schedule_prefix: Int,
    @SerializedName("schedule_code") val schedule_code: Int,
    @SerializedName("schedule_exec") val schedule_exec: Int,
    @SerializedName("comments") val comments:String,
    @SerializedName("justify_group_code") val justify_group_code: Int,
    @SerializedName("justify_item_code") val justify_item_code: Int,
    @SerializedName("reschedule_date") val reschedule_date: String,
): Main_Header_Env(
    app_code,
    app_version,
    app_type,
    session_app
)
