package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.ui.act093.model.InfoSerialModel.Companion.SEPARATOR

private const val SEPARATOR = " | "

fun List<String>.formatPipeFields(): String {
    return this.joinToString(SEPARATOR)
}

fun Array<String>.formatPipeFields(): String {
    return this.joinToString(SEPARATOR)
}
