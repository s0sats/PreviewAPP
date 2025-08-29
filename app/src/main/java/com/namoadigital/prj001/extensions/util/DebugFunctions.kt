package com.namoadigital.prj001.extensions.util

import com.namoa_digital.namoa_library.util.ConstantBase.LIB_SUPPORT_PATH
import java.io.File

fun saveText(content: String) {
    val file = File(LIB_SUPPORT_PATH, "debug.txt")
    file.appendText(content +"\n")
}