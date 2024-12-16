package com.namoadigital.prj001.extensions

fun Int.isZero() = this == 0
fun Int?.isZeroOrNull() = this == null || this == 0
fun Int?.ifZero(block: () -> Int){
    if (!this.isZeroOrNull()) block()
}