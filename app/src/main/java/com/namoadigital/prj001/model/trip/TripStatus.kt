package com.namoadigital.prj001.model.trip

enum class
TripStatus {

    PENDING,
    TRANSIT,
    TRANSFER,
    ON_SITE,
    OVER_NIGHT,
    WAITING_DESTINATION,
    CANCELLED,
    START,
    DONE,
    NULL,

}

fun String.toTripStatus() = when (this) {
    "PENDING" -> TripStatus.PENDING
    "TRANSIT" -> TripStatus.TRANSIT
    "TRANSFER" -> TripStatus.TRANSFER
    "OVER_NIGHT" -> TripStatus.OVER_NIGHT
    "ON_SITE" -> TripStatus.ON_SITE
    "WAITING_DESTINATION" -> TripStatus.WAITING_DESTINATION
    "CANCELLED" -> TripStatus.CANCELLED
    "START" -> TripStatus.START
    "DONE" -> TripStatus.DONE
    else -> TripStatus.NULL
}

fun TripStatus.toDescription() = when(this){
    TripStatus.PENDING -> "PENDING"
    TripStatus.TRANSIT -> "TRANSIT"
    TripStatus.TRANSFER -> "TRANSFER"
    TripStatus.OVER_NIGHT -> "OVER_NIGHT"
    TripStatus.ON_SITE -> "ON_SITE"
    TripStatus.WAITING_DESTINATION -> "WAITING_DESTINATION"
    TripStatus.CANCELLED -> "CANCELLED"
    TripStatus.START -> "START"
    TripStatus.DONE -> "DONE"
    TripStatus.NULL -> "NULL"
}