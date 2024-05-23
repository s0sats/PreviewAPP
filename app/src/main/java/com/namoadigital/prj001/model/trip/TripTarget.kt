package com.namoadigital.prj001.model.trip

enum class TripTarget(val type: String) {
    START("START"),
    DESTINATION("DESTINATION"),
    END("END")
}

fun String.toTripTarget() = when(this){
    "START" -> TripTarget.START
    "DESTINATION" -> TripTarget.DESTINATION
    "END" -> TripTarget.END
    else -> null
}