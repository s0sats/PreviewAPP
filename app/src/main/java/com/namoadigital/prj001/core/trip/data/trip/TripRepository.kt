package com.namoadigital.prj001.core.trip.data.trip

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.TripStatusChangeEnv
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.di.model.OriginSites

interface TripRepository {
    fun getPreference(): CurrentTripPrefModel
    fun setPreference(
        customerCode: Long?,
        tripPrefix: Int?,
        tripCode: Int?,
        tripScn: Int?,
    )

    fun getTrip(): FSTrip?

    fun getOriginCoordinates() : Coordinates?

    fun getTripStatus(): TripStatus?
    fun setTripStatus(request: TripStatusChangeEnv)
    fun getPositionDistanceMin(): Double

    fun execCreateTrip(input: Coordinates?)
    fun execSyncTrip()
    fun execSaveFleetData(
        licensePlate: String? = null,
        odometer: Long?,
        path: String?,
        changePhoto: Int,
        target: String,
        destinationSeq: Int? = null,
        deletePhoto: Boolean,
    )

    fun getListSites(): List<OriginSites>

    fun execOriginSet(
        date: String,
        originType: OriginType,
        coordinates: Coordinates?,
        siteCode: Int?,
        siteDesc: String?
    )

    fun getEvent(): FSTripEvent?
    fun getExtract(trip: FSTrip?): Extract<FSTrip>?
}