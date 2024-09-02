package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class TkTicketAddress(
    @SerializedName("country") val country: String?,
    @SerializedName("countryId") val countryId: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("district") val district: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("streetnumber") val streetnumber: String?,
    @SerializedName("complement") val complement: String?,
    @SerializedName("zipcode") val zipcode: String?,
    @SerializedName("plus_code") val plus_code: String?,
    @SerializedName("latitude") val latitude: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("contact") val contact: String?,
    @SerializedName("phone") val phone: String?,
)