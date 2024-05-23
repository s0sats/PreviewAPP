package com.namoadigital.prj001.core.trip.data.position

import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel

interface TripPositionRepository {

    fun execPositionSave()

    fun getSavedDate(): String?

    fun getLocationSave(): Coordinates?
    fun getLocationRefSave(): Coordinates?
    fun changeStatusNotification(alertType: TripPositionAlertType)
    fun getLastAlertType(): TripPositionAlertType?

    fun savePosition(
        trip: FSTrip,
        coordinates: Coordinates,
        distance: Double?,
        updateRequired: Int,
        alertType: TripPositionAlertType?,
        tripDestinationSeq: Int?,
        isRef: Int
    )
    fun updateIsRefPosition(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        tripPositionSeq: Int,
        isRef: Int
    )


    fun isUpdateRequired():Boolean
    fun getLocationPref(): CurrentTripPrefModel?
    fun setLasLocationRef()
}