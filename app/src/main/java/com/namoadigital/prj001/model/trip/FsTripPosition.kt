package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FsTripPosition(
    @SerializedName("customerCode") val customerCode: Long,
    @SerializedName("tripPrefix") val tripPrefix: Int,
    @SerializedName("tripCode") val tripCode: Int,
    @SerializedName("tripPositionSeq") val tripPositionSeq: Int, //SEQ
    @SerializedName("tripDestinationSeq") val tripDestinationSeq: Int?, //SEQ do Destino.
    @SerializedName("tripPositionLat") val tripPositionLat: Double?, //LAT ATUAL
    @SerializedName("tripPositionLon") val tripPositionLon: Double?, //LONG ATUAL
    @SerializedName("tripPositionDate") val tripPositionDate: String?, //DATA DE SAVE
    @SerializedName("tripPositionAlertType") val tripPositionAlertType: String?,
    @SerializedName("updateRequired") val updateRequired: Int, //STATUS TRIP
    @SerializedName("tripPositionSpeed") val tripPositionSpeed: Double?, //VELOCIDADE DO USER
    @SerializedName("tripPositionDistance") val tripPositionDistance: Double?, //DISTANCIA ENTRE O PONTO ATUAL E O DESTINO
    @SerializedName("isRef") var isRef: Int=0, //
    val token: String? = null,
)

