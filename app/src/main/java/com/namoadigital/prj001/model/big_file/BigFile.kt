package com.namoadigital.prj001.model.big_file

import com.google.gson.annotations.SerializedName

data class BigFile(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_code") var fileCode: Int? = null,
    @SerializedName("file_status") var fileStatus: String? = null,
    @SerializedName("file_url") var fileUrl: String? = null,
    @SerializedName("file_md5") var fileMd5: String? = null,
){
    companion object{
        const val FILE_TYPE = "file_type"
        const val FILE_CODE = "file_code"
        const val FILE_STATUS = "file_status"
        const val FILE_URL = "file_url"
        const val FILE_MD5 = "file_md5"
    }
}
