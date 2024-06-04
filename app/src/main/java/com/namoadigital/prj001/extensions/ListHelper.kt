package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.SEPARATOR
import java.util.HashSet

private const val SEPARATOR = " | "

fun List<String>.formatPipeFields(): String {
    return this.joinToString(SEPARATOR)
}

fun Array<String>.formatPipeFields(): String {
    return this.joinToString(SEPARATOR)
}

fun HashSet<String>.containFileName(searchFilename: String): Boolean {
    return this.contains(searchFilename)
}
