package com.namoadigital.prj001.model.big_file.create_file

import com.google.gson.annotations.SerializedName

data class BigFileContentRec(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_code") var fileCode: Int,
    @SerializedName("file_status") var fileStatus: String,
    @SerializedName("file_url") var file_url: String?,
    @SerializedName("file_md5") var file_md5: String?,
)
