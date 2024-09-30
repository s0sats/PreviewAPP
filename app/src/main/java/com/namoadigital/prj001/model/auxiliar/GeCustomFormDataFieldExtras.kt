package com.namoadigital.prj001.model.auxiliar

import com.google.gson.annotations.SerializedName

data class GeCustomFormDataFieldExtras(
    @SerializedName("CONTENT") val content:List<GeCustomFormDataFieldExtrasContent>
){
    data class GeCustomFormDataFieldExtrasContent(
        @SerializedName("PHOTO1") val photo1:String?,
        @SerializedName("PHOTO2") val photo2:String?,
        @SerializedName("PHOTO3") val photo3:String?,
        @SerializedName("PHOTO4") val photo4:String?,
    )
}