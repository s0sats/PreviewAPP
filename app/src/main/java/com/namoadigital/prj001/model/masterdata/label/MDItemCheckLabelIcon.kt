package com.namoadigital.prj001.model.masterdata.label

import com.google.gson.annotations.SerializedName

class MDItemCheckLabelIcon (
    @SerializedName("label_icon_id") val labelIconId :String,
    @SerializedName("label_icon") val labelIcon :String,
    @SerializedName("label_icon_desc") val labelIconDesc :String,
)