package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.model.SM_SO
import java.text.Normalizer

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

