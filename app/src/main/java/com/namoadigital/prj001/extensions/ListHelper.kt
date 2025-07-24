package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.model.GE_File
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

fun List<GE_File>.findFilePath(fileName: String):Boolean{
    val filenameFromList = this.map {
        it.file_code
    }
    return filenameFromList.contains(fileName)
}

fun <T> listToHashMap(list: List<T>, pageSize: Int = 30): HashMap<Int, List<T>> {
    val page = HashMap<Int, List<T>>()
    val totalPage = (list.size + pageSize - 1) / pageSize

    for (i in 0 until totalPage) {
        val start = i * pageSize
        val end = minOf(start + pageSize, list.size)
        page[i + 1] = list.subList(start, end)
    }

    return page
}
