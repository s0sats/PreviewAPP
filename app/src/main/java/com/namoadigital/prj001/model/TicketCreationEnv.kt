package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TicketCreationEnv (
    app_code: String,
    app_version: String,
    app_type: String,
    session_app: String,
    @SerializedName("token") val token: String,
    @SerializedName("ticket") var ticket: ArrayList<TicketCreationParams>
): Main_Header_Env(
    app_code,
    app_version,
    app_type,
    session_app
)
