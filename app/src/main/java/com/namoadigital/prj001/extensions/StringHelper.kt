package com.namoadigital.prj001.extensions

import java.text.Normalizer
import java.util.regex.Pattern

fun String?.formatForDisplay() = if (this.isNullOrBlank()) "" else this


fun String.stripAccents(): String {
    var string = Normalizer.normalize(this, Normalizer.Form.NFD)
    string = Regex("\\p{InCombiningDiacriticalMarks}+").replace(string, "")
    return string
}

fun checkIfHasCharInvalid(value: String) =
    Pattern.compile("/^\\s|\\s$|\\s{2}|[\\t\\n\\r]|[^\\s0-9a-zà-ü\\-\\_\\(\\)\\[\\]\\{\\}\\.\\|\\/\\+\\\\]/gmi")
        .matcher(value).find()
