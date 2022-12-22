package com.namoadigital.prj001.ui.act093.util

sealed class Act093Event {

    object OnUpdateScreen : Act093Event()
    object OnUpdateList : Act093Event()

    data class Toast(val message: String) : Act093Event()

}
