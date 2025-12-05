package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TMDProductSerialStructureObj(
    @SerializedName("structure")
    @Expose
    val structure: List<MD_Product_Serial_Structure>?
)