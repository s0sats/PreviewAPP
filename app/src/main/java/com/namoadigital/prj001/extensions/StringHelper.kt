package com.namoadigital.prj001.extensions

fun String?.formatForDisplay() = if(this.isNullOrBlank()) "" else this