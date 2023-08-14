package com.namoadigital.prj001.extensions

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
