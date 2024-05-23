package com.namoadigital.prj001.ui.act005.trip.repository.mapping

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.adapter.trip.model.ExtractType
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.model.trip.AvailableUsersRec

fun AvailableUsersRec.toListAdapter() = TripUserEdit(
    userCode = this.userCode!!,
    userNick = this.userNick!!,
    userName = this.userName!!
)

fun FSTripUser.toExtract() = Extract(
    type = ExtractType.USER,
    dateStart = this.dateStart,
    filter = this.userName,
    model = this
)

fun FSTripEvent.toExtract() = Extract(
    type = ExtractType.EVENT,
    dateStart = this.eventStart,
    filter = this.eventTypeDesc ?: "",
    model = this
)

fun FsTripDestination.toExtract() = Extract(
    type = ExtractType.DESTINATION,
    dateStart = this.arrivedDate,
    filter = this.destinationSiteDesc ?: "",
    model = this
)

fun FSTrip.toOriginExtract() = Extract(
    type = ExtractType.ORIGIN,
    dateStart = this.originDate,
    filter = this.originSiteDesc ?: "",
    model = this
)

fun FsTripDestinationAction.toActionExtract() = Extract(
    type = ExtractType.ACTION,
    dateStart = this.dateStart,
    filter = this.actDesc ?: "",
    model = this
)