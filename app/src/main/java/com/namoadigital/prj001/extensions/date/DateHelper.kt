package com.namoadigital.prj001.extensions.date

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateApi() = ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)

fun String.convertDateToFullTimeStampGMT(inputFormat: String = DATE_FORMAT, outputFormat: String = FULL_TIMESTAMP_TZ_FORMAT_GMT): String {
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
    }catch (e: Exception){
        ToolBox_Inf.registerException("CalculateMinutesBetweenDates", e)
        -1L
    }
}

