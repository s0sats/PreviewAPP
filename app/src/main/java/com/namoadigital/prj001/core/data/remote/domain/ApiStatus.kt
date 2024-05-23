package com.namoadigital.prj001.core.data.remote.domain

import com.google.gson.annotations.SerializedName

data class ApiStatus(
    @SerializedName("code") var code: Int,
    @SerializedName("message") var message: String,
    @SerializedName("tip") var tip: String
) {
    val serverMessage = """
            $message
            $tip 
        """
}