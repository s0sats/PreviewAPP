package com.namoadigital.prj001.model.trip

enum class TripPositionAlertType{
    ARRIVED,
    DEPARTED,
    WAITING_DESTINATION,
    PENDING,
    NULL
}

fun TripPositionAlertType.toDescription() = when(this){
    TripPositionAlertType.PENDING -> "PENDING"
    TripPositionAlertType.ARRIVED -> "ARRIVED"
    TripPositionAlertType.DEPARTED -> "DEPARTED"
    TripPositionAlertType.WAITING_DESTINATION -> "WAITING_DESTINATION"
    TripPositionAlertType.NULL -> null
}

fun String?.toAlertType() = when(this){
    "PENDING" -> TripPositionAlertType.PENDING
    "ARRIVED" -> TripPositionAlertType.ARRIVED
    "DEPARTED" -> TripPositionAlertType.DEPARTED
    "WAITING_DESTINATION" -> TripPositionAlertType.WAITING_DESTINATION
    else -> TripPositionAlertType.NULL
}