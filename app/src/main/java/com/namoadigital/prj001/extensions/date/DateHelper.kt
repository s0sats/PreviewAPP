package com.namoadigital.prj001.extensions.date

import android.content.Context
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue


fun getCurrentDateApi(resetSeconds: Boolean = false): String {
    val calendar = Calendar.getInstance()
    if (resetSeconds) {
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
    val date: Date? = calendar.getTime()
    val sdf = SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT, Locale.getDefault())
    return sdf.format(date)
}

fun String.convertDateToFullTimeStampGMT(
    inputFormat: String = DATE_FORMAT,
    outputFormat: String = FULL_TIMESTAMP_TZ_FORMAT_GMT
): String {
    val sdfInput = SimpleDateFormat(inputFormat)
    val sdfOutput = SimpleDateFormat(outputFormat)
    var formattedDate: String = ""
    if (this.trim().isNotEmpty() && !this.equals("null", ignoreCase = true)) {
        try {
            val parseDate = sdfInput.parse(this)
            formattedDate = parseDate?.let { sdfOutput.format(it) }!!
        } catch (e: ParseException) {
            ToolBox_Inf.registerException("DateHelper", e)
        }
    }
    return formattedDate
}


fun String.convertToDate(inputFormat: String = FULL_TIMESTAMP_TZ_FORMAT_GMT): Date? {
    val sdfInput = SimpleDateFormat(inputFormat, Locale.getDefault())
    return if (this.trim().isNotEmpty() && !this.equals("null", ignoreCase = true)) {
        try {
            sdfInput.parse(this)
        } catch (e: ParseException) {
            null
        }
    } else {
        null
    }
}


fun String.toFormattedDateAndTime(): Date? {
    val format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

fun Date?.toFormattedString(): String {
    val format = SimpleDateFormat(FULL_TIMESTAMP_TZ_FORMAT_GMT, Locale.getDefault())
    return this?.let { format.format(it) } ?: ""
}


fun Context.formatDate(type: FormatDateType): String {
    when (type) {
        is FormatDateType.OnlyDate -> {
            return ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(type.value),
                ToolBox_Inf.nlsDateFormat(this)
            )
        }

        is FormatDateType.OnlyHour -> {
            return ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(type.value),
                "HH:mm"
            )
        }

        is FormatDateType.DateAndHour -> {
            return ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(type.value),
                ToolBox_Inf.nlsDateFormat(this) + " HH:mm"
            )
        }
    }
}

fun calculateMinutesBetweenDates(
    startDate: String,
    endDate: String,
    format: String
): Long {
    return try {
        val sdf = SimpleDateFormat(format, Locale.getDefault())

        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)

        val differenceInMillis = end!!.time - start!!.time
        differenceInMillis / (1000 * 60)
    } catch (e: Exception) {
        ToolBox_Inf.registerException("CalculateMinutesBetweenDates", e)
        -1L
    }
}


fun compareDates(
    startDate: String,
    endDate: String,
    startDateFormat: String = ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
    endDateFormat: String = ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
    comparator: (Date, Date) -> Boolean
): Boolean {
    val startDateFormat = SimpleDateFormat(startDateFormat, Locale.getDefault())
    val endDateFormat = SimpleDateFormat(endDateFormat, Locale.getDefault())
    return try {
        if (startDate.trim().isEmpty() || endDate.trim().isEmpty()) return false
        val dateStart = startDateFormat.parse(startDate)
        val dateEnd = endDateFormat.parse(endDate)
        dateStart?.let { start ->
            dateEnd?.let { end ->
                comparator(start, end)
            }
        } ?: false
    } catch (e: Exception) {
        ToolBox_Inf.registerException("compareDate", e)
        false
    }
}

private fun String.compareDateToCurrent(
    startFormatDate: String = FULL_TIMESTAMP_TZ_FORMAT_GMT,
    comparator: (Date, Date) -> Boolean
): Boolean {
    val simpleDate = SimpleDateFormat(startFormatDate)
    val currentSimpleDate = SimpleDateFormat(FULL_TIMESTAMP_TZ_FORMAT_GMT)
    return try {
        if (this.trim().isEmpty()) return false
        val userDate = simpleDate.parse(this)
        val dialogDate = currentSimpleDate.parse(getCurrentDateApi())
        userDate?.let { user ->
            dialogDate?.let { current ->
                comparator(user, current)
            }
        } ?: false
    } catch (e: Exception) {
        ToolBox_Inf.registerException("compareDateToCurrent", e)
        false
    }
}

fun String.dateIsFuture(): Boolean {
    return this.compareDateToCurrent { userDate, currentDate ->
        userDate.after(currentDate)
    }
}

fun isDateBeforeOrEquals(startDate: String, endDate: String?): Boolean {
    return endDate?.let { date ->
        compareDates(
            startDate,
            date
        ) { startDate, threshold -> startDate.before(threshold) || startDate == threshold }
    } ?: false
}

fun isDateBefore(startDate: String, endDate: String?): Boolean {
    return endDate?.let { date ->
        compareDates(
            startDate,
            date
        ) { startDate, threshold -> startDate.before(threshold) }
    } ?: false
}

fun isDateEquals(startDate: String, endDate: String?): Boolean {
    return endDate?.let { date ->
        compareDates(
            startDate,
            date
        ) { startDate, threshold -> startDate == threshold }
    } ?: false
}

fun String.getDateDiferenceInMinutes(secoundDate: String): Long {
    return (ToolBox_Inf.getDateDiferenceInMilliseconds(this, secoundDate) / 60000).absoluteValue
}