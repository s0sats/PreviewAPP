package com.namoadigital.prj001.extensions.date

sealed class FormatDateType(open val value: String) {
    data class OnlyDate(override val value: String) : FormatDateType(value)
    data class OnlyHour(override val value: String) : FormatDateType(value)
    data class DateAndHour(override val value: String) : FormatDateType(value)
}
