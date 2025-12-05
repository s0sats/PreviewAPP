package com.namoadigital.prj001.model.big_file.create_file

import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure_Env
import java.util.ArrayList

data class BigFileStructureCreationEnv(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("structure") var structure:ArrayList<T_MD_Product_Serial_Structure_Env>,
): Main_Header_Env()
