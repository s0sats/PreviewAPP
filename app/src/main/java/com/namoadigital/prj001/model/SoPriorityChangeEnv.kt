package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class SoPriorityChangeEnv(
    app_code: String,
    app_version: String,
    app_type: String,
    session_app: String,
    @SerializedName("token") val token: String,
    @SerializedName("so_prefix") val so_prefix: Int,
    @SerializedName("so_code") val so_code: Int,
    @SerializedName("so_scn") val so_scn: Int,
    @SerializedName("priority_code") val priority_code: Int
): Main_Header_Env(
    app_code,
    app_version,
    app_type,
    session_app
)
