package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FsTripPosition(
    @Expose @SerializedName("customerCode") val customerCode: Long,
    @Expose @SerializedName("tripPrefix") val tripPrefix: Int,
    @Expose @SerializedName("tripCode") val tripCode: Int,
    @Expose @SerializedName("tripPositionSeq") val tripPositionSeq: Int, //SEQ
    @Expose @SerializedName("tripDestinationSeq") val tripDestinationSeq: Int?, //SEQ do Destino.
    @Expose @SerializedName("tripPositionLat") val tripPositionLat: Double?, //LAT ATUAL
    @Expose @SerializedName("tripPositionLon") val tripPositionLon: Double?, //LONG ATUAL
    @Expose @SerializedName("tripPositionDate") val tripPositionDate: String?, //DATA DE SAVE
    @Expose @SerializedName("tripPositionAlertType") val tripPositionAlertType: String?,
    @Expose @SerializedName("updateRequired") val updateRequired: Int, //STATUS TRIP
    @Expose @SerializedName("tripPositionSpeed") val tripPositionSpeed: Double?, //VELOCIDADE DO USER
    @Expose @SerializedName("tripPositionDistance") val tripPositionDistance: Double?, //DISTANCIA ENTRE O PONTO ATUAL E O DESTINO
    @Expose @SerializedName("isRef") var isRef: Int=0, //
    val token: String? = null,
)

