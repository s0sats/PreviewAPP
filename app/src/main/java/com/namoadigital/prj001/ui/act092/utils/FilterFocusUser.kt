package com.namoadigital.prj001.ui.act092.utils

data class FilterFocusUser(
    var mainUser: Boolean = false,
    var userFocus: Boolean = true,
) {
    val userFocusInt = if (userFocus) 0 else 1

}