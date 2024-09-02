package com.namoadigital.prj001.model.trip.mapping

import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.FSTripUserFullUpdateEnv

fun FSTripUser.toTripUpdate() = FSTripUserFullUpdateEnv(
    userSeq = this.userSeq,
    userCode = this.userCode,
    dateStart = this.dateStart,
    dateEnd = this.dateEnd,
)