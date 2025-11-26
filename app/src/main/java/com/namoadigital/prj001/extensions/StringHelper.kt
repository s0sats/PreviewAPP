package com.namoadigital.prj001.extensions

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.model.SM_SO
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun String?.formatForDisplay() = if (this.isNullOrBlank()) "" else this


fun String.stripAccents(): String {
    var string = Normalizer.normalize(this, Normalizer.Form.NFD)
    string = Regex("\\p{InCombiningDiacriticalMarks}+").replace(string, "")
    return string
}

fun checkIfHasCharInvalid(value: String): Boolean {
    val regexPattern = Regex(
        """^\s|\s$|\s{2}|[\t\n\r]|[^\s0-9a-zà-ü\-\_\(\)\[\]\{\}\.\|\/\+\\\ª\º]""",
        setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )
    return regexPattern.containsMatchIn(value)
}

fun String?.parseDatePair(): Pair<String, String> {
    if (this == null) return Pair("", "")
    return try {
        val old = SimpleDateFormat(
            ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
            Locale.getDefault()
        ).parse(this)
        val newDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(old!!)
        val newHour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(old)
        Pair(newDate, newHour)
    } catch (e: Exception) {
        ToolBox_Inf.registerException("StringHelper (parseDatePair)", e)
        Pair("", "")
    }
}

fun String.parseDate(): String {
    return try {
        val old = SimpleDateFormat(
            ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT,
            Locale.getDefault()
        ).parse(this)
        val newDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(old!!)
        val newHour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(old)
        "$newDate $newHour"
    } catch (e: Exception) {
        ToolBox_Inf.registerException("StringHelper (parseDate)", e)
        ""
    }
}

fun String.parseFullDate(withSeconds: Boolean = true): String {
    return try {
        val dateFormat = if (withSeconds) "dd/MM/yy HH:mm:ss" else "dd/MM/yy HH:mm"
        val formatDate = "$this:00"
        val simpleDate = SimpleDateFormat(dateFormat, Locale.getDefault())
        val currentSimpleDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val old = simpleDate.parse(if (withSeconds) formatDate else this)
        val gmtDate = currentSimpleDate.format(old!!)
        val gmtValue = ToolBox.getDeviceGMT(false)
        "$gmtDate $gmtValue"
    } catch (e: Exception) {
        ToolBox_Inf.registerException("StringHelper (parseFullDate)", e)
        ""
    }

}

fun checkIfLastCharContainTabOrEnter(value: String): Boolean {
    Regex(
        """[ \t\n\r]*([\t\n\r])${'$'}""",
        setOf(RegexOption.COMMENTS, RegexOption.DOT_MATCHES_ALL)
    ).let { regex ->
        return regex.containsMatchIn(value)
    }
}

fun removeLastCharEnterOrTab(value: String): String {
    return value.replace("""[ \t\n\r]*([\t\n\r])${'$'}""", "")
}

fun formatSyncSoList(soSyncList: List<SM_SO>): String {
    var serviceSoList = StringBuilder()
    for (sm_so in soSyncList) {
        serviceSoList.append("|").append(sm_so.so_prefix).append(".").append(sm_so.so_code)
    }
    serviceSoList = StringBuilder(serviceSoList.substring(1))
    return serviceSoList.toString()
}

fun String.formatTo(format: String): String {
    return ToolBox_Inf.millisecondsToString(
        ToolBox_Inf.dateToMilliseconds(
            this
        ),
        format
    )

}

fun addHourToDateLimited(): String {
    var currentDateTime = Calendar.getInstance()
    val endOfDay = currentDateTime.clone() as Calendar

    currentDateTime.add(Calendar.HOUR_OF_DAY, 1)
    endOfDay[Calendar.HOUR_OF_DAY] = 23
    endOfDay[Calendar.MINUTE] = 59
    endOfDay[Calendar.SECOND] = 59
    endOfDay[Calendar.MILLISECOND] = 0
    TimeZone.setDefault(TimeZone.getTimeZone(ToolBox.getDeviceGMT(true)))

    if (currentDateTime.after(endOfDay)) {
        currentDateTime = endOfDay
    }

    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")

    return formatter.format(currentDateTime.time)

}

fun String.isValidDecimalInput(maxDecimalPlaces: Int = 2): Boolean {
    if (this.isEmpty()) return true // Permite campo vazio

    // Regex para número decimal com até 'maxDecimalPlaces' casas
    // Permite: "", "1", "123", "1.2", "12.34", "." (enquanto digita), "1."
    // Não permite: "1.234", "abc", "1..2"
    val regexPattern = "^\\d*\\.?\\d{0,$maxDecimalPlaces}$"
    return this.matches(Regex(regexPattern))
}

fun getFormattedAddress(address: String): String {
    return address.trim()
        .replace("-", "")
        .replace(" ", "+")
}