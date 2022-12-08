package com.namoadigital.prj001.ui.act092.utils

data class FilterFocusUser(
    var mainUser: Boolean = false,
    var userFocus: Boolean = false,
) {

    val userFocusInt = if (userFocus) 1 else 0

}