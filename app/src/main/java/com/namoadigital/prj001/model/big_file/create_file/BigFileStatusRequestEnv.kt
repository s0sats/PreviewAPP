package com.namoadigital.prj001.model.big_file.create_file

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env

data class BigFileStatusRequestEnv(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_code") val fileCode:Int,
) : Main_Header_Env()