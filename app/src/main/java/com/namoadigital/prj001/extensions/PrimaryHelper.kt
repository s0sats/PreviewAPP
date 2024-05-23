package com.namoadigital.prj001.extensions

fun String?.toLongOrNegative(): Long{
    return if(this.isNullOrEmpty()) -1L
    else this.toLong()
}

fun String?.toIntOrNegative(): Int {
    return if(this.isNullOrEmpty()) -1
    else this.toInt()
}