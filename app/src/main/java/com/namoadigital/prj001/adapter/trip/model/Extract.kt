package com.namoadigital.prj001.adapter.trip.model

data class Extract<T>(
    val type: ExtractType,
    val dateStart: String?,
    val filter: String,
    val model: T,
)


enum class ExtractType {
    ORIGIN,
    DESTINATION,
    ACTION,
    USER,
    EVENT,
}