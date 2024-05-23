package com.namoadigital.prj001.model.location

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("date") var date: String? = null
){
    val isDefault = latitude == 0.0 && longitude == 0.0
}

fun defaultCoordinates() = Coordinates(0.0, 0.0)