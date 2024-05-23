package com.namoadigital.prj001.adapter.trip.mapping

import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit

fun FSTripUser.toUserEdit() = TripUserEdit(
    userCode = this.userCode,
    userName = this.userName,
    userNick = "",
    userSeq = this.userSeq,
    dateStart = this.dateStart,
    dateEnd = this.dateEnd

)