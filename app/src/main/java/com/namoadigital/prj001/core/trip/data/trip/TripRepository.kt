package com.namoadigital.prj001.core.trip.data.trip

import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripFullUpdateEnv
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.ui.act005.trip.di.model.OriginSites
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getPreference(): CurrentTripPrefModel
    fun setPreference(
        customerCode: Long?,
        tripPrefix: Int?,
        tripCode: Int?,
        tripScn: Int?,
    )

    fun getTrip(): FSTrip?

    fun getOriginCoordinates(): Coordinates?

    fun getTripStatus(): TripStatus?
    fun setTripStatus(
        tripStatus: TripStatus,
        nextTripStatus: String,
        destinationSeq: Int?,
        destinationStatus: String?,
        nextDestinationSeq: Int?,
        nextDestinationStatus: String?,
        endDate: String?,
    ): Flow<IResult<Unit>>

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
    ): Flow<IResult<Unit>>

    fun getListSites(): List<OriginSites>

    suspend fun execOriginSet(
        date: String,
        originType: OriginType,
        coordinates: Coordinates?,
        siteCode: Int?,
        siteDesc: String?
    ): Flow<IResult<Unit>>

    fun getEvent(): FSTripEvent?
    fun getExtract(trip: FSTrip?): List<Extract<FSTrip>>?
    fun getTripByDestinationSeq(destinationSeq: Int): FSTrip?
    fun getTripFullUpdateEnv(trip: FSTrip): FSTripFullUpdateEnv?
    fun sendTripFullUpdate(): Boolean
    fun getTripUpdateRequired(): Boolean
    fun existsTripWithUpdateRequired(): Boolean
    fun isTripOnline(it: FSTrip): Boolean
    fun saveStartDateSet(date: String): Flow<IResult<Unit>>

    fun getUsersCurrentTrip(
        tripPrefix: Int,
        tripCode: Int,
    ): List<FSTripUser>

}