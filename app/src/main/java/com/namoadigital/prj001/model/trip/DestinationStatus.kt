package com.namoadigital.prj001.model.trip

enum class DestinationStatus {
    PENDING,
    ARRIVED,
    DEPARTED,
    TRANSIT,
    WAITING_DESTINATION,
    CANCELLED,
    NULL
}

fun String.toDestinationStatus() = when (this) {
    "PENDING" -> DestinationStatus.PENDING
    "ARRIVED" -> DestinationStatus.ARRIVED
    "DEPARTED" -> DestinationStatus.DEPARTED
    "TRANSIT" -> DestinationStatus.TRANSIT
    "WAITING_DESTINATION" -> DestinationStatus.WAITING_DESTINATION
    "CANCELLED" -> DestinationStatus.CANCELLED
    else -> DestinationStatus.NULL
}

fun DestinationStatus.toDescription() = when (this) {
    DestinationStatus.PENDING -> "PENDING"
    DestinationStatus.ARRIVED -> "ARRIVED"
    DestinationStatus.DEPARTED -> "DEPARTED"
    DestinationStatus.TRANSIT -> "TRANSIT"
    DestinationStatus.WAITING_DESTINATION -> "WAITING_DESTINATION"
    DestinationStatus.CANCELLED -> "CANCELLED"
    DestinationStatus.NULL -> ""
}