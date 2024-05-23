package com.namoadigital.prj001.ui.act005.trip.di.model

import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction

data class TripUser(
    val userCode: Int,
    val userNick: String,
    val userName: String,
    val userSeq: Int? = null,
    val typeAction: UserAction? = null,
    val inDate: String = "",
    val outDate: String = ""
)