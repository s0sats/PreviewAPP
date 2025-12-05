package com.namoadigital.prj001.model.big_file.create_file

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Rec

data class BigFileRec(
    @SerializedName("file") val file: BigFileContentRec,
): Main_Header_Rec()
