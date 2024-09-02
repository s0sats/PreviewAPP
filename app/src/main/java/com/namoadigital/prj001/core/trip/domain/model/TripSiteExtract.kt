package com.namoadigital.prj001.core.trip.domain.model

data class TripSiteExtract<T : Any>(
    val formStatus: FormStatus,
    val date: String,
    val model: T
)

enum class FormStatus(val weight: Int) {
    IN_PROCESS(0),
    WAITING_SYNC(1),
    DONE(2)
}