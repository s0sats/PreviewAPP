package com.namoadigital.prj001.model.masterdata.ge_os.vg

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FormVgs(
    @Expose
    @SerializedName("customer_code") var customerCode: Long,
    @Expose
    @SerializedName("custom_form_type") var customFormType: Int,
    @Expose
    @SerializedName("custom_form_code") var customFormCode: Int,
    @Expose
    @SerializedName("custom_form_version") var customFormVersion: Int,
    @Expose
    @SerializedName("custom_form_data") var customFormData: Int,
    @Expose
    @SerializedName("vg_code") var vgCode: Int
)