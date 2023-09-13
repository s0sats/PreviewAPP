package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

data class SerialSiteEnv(
    @SerializedName("site_code") var site_code: Int,
) : Main_Header_Env()
