package com.namoadigital.prj001.core.trip.data.position

import android.content.Context
import android.util.Log
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripPositionDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.sendCommandToServiceTripLocation
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripPosition
import com.namoadigital.prj001.model.trip.TripPositionAlertType
import com.namoadigital.prj001.model.trip.preference.CurrentTripPrefModel
import com.namoadigital.prj001.model.trip.toAlertType
import com.namoadigital.prj001.model.trip.toDescription
import com.namoadigital.prj001.receiver.trip.WBR_UserPosition
import com.namoadigital.prj001.service.location.util.calculateSpeed
import com.namoadigital.prj001.sql.trip.PositionUpdateIsReq
import com.namoadigital.prj001.util.ToolBox_Con
import javax.inject.Inject

class TripPositionRepositoryImp @Inject constructor(
    private val context: Context,
    private val dao: FsTripPositionDao,
    private val tripDao: FSTripDao,
    private val currentTripPref: CurrentTripPref
) : TripPositionRepository {
    override fun savePosition(
        trip: FSTrip,
        coordinates: Coordinates,
        distance: Double?,
        updateRequired: Int,
        alertType: TripPositionAlertType?,
        tripDestinationSeq: Int?,
        isRef: Int
    ) {
        val positionSeq = dao.getPositionSeq()
        //
        val coordinatesSaved = getLastPosition()
        /*val coordinatesSaved = if(trip.tripStatus.toTripStatus() == TripStatus.TRANSFER
            || trip.tripStatus.toTripStatus() == TripStatus.WAITING_DESTINATION)
        {
            getLocationRefSave()
        }else {
            getLastPosition()
        }
        *///
        FsTripPosition(
            customerCode = context.getCustomerCode(),
            tripPrefix = trip.tripPrefix,
            tripCode = trip.tripCode,
            tripPositionSeq = positionSeq,
            tripDestinationSeq = tripDestinationSeq,
            tripPositionLat = coordinates.latitude,
            tripPositionLon = coordinates.longitude,
            tripPositionDate = coordinates.date,
            tripPositionSpeed = coordinatesSaved?.let {
                if (it.isDefault) null else calculateSpeed(coordinates, it)
            },
            tripPositionDistance = distance,
            tripPositionAlertType = alertType?.toDescription(),
            updateRequired = updateRequired,
            isRef = isRef
        ).let { model ->
            dao.addUpdate(model)
            val preference = currentTripPref.read()
            preference.position_seq = positionSeq
            preference.last_latitude = coordinates.latitude
            preference.last_longitude = coordinates.longitude
            preference.last_location_date = coordinates.date
            currentTripPref.write(preference)
        }
    }

    override fun updateIsRefPosition(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        tripPositionSeq: Int,
        isRef: Int
    ) {
        dao.addUpdate(
            PositionUpdateIsReq(
                customerCode,
                tripPrefix,
                tripCode,
                tripPositionSeq,
                isRef,
            ).toSqlQuery()
        )
    }


    override fun isUpdateRequired(): Boolean {
        return dao.getAllUpdateRequired().isNotEmpty()
    }

    override fun execPositionSave() {
        if (ToolBox_Con.isOnline(context)
            && dao.getAllUpdateRequired().isNotEmpty()) {
            context.sendToWebServiceReceiver<WBR_UserPosition>()
        }
    }

    private fun getLastPosition(): Coordinates? {
        val pref = CurrentTripPref.invoke(context)
        val model = pref.read()
        val trip = tripDao.getTrip()
        trip?.let {
            if (model.customer_code == it.customerCode
                && model.trip_prefix == it.tripPrefix
                && model.trip_code == it.tripCode
            ) {
                return Coordinates(
                    model.last_latitude,
                    model.last_longitude,
                    model.last_location_date,
                )
            }
            pref.clear()
        }
        return null
    }
    override fun getSavedDate(): String? {
        val pref = CurrentTripPref.invoke(context)
        val model = pref.read()
        val trip = tripDao.getTrip()
        trip?.let {
            if (model.customer_code == it.customerCode
                && model.trip_prefix == it.tripPrefix
                && model.trip_code == it.tripCode
            ) {
                return model.last_location_date
            }
            pref.clear()
        }
        return null
    }

    override fun getLocationPref(): CurrentTripPrefModel? {
        val pref = CurrentTripPref.invoke(context)
        val model = pref.read()
        val trip = tripDao.getTrip()

        return trip?.let {
            if (checkCurrentTrip(model, it)) {
                model
            } else {
                null
            }
        }
    }

    override fun setLasLocationRef() {
        dao.setLastPositionAsReference()
    }

    override fun getLocationSave(): Coordinates? {
        val locationPref = getLocationPref()
        return locationPref?.coordinates
    }

    override fun getLocationRefSave(): Coordinates? {
        val locationPref = getLocationPref()
        return locationPref?.coordinatesRef
    }

    private fun checkCurrentTrip(
        model: CurrentTripPrefModel,
        it: FSTrip
    ) = (model.customer_code == it.customerCode
            && model.trip_prefix == it.tripPrefix
            && model.trip_code == it.tripCode)

    override fun changeStatusNotification(alertType: TripPositionAlertType) {
        context.sendCommandToServiceTripLocation(alertType.toDescription().orEmpty())
    }

    override fun getLastAlertType(): TripPositionAlertType? {
        val pref = CurrentTripPref.invoke(context)
        val lastAlertType = pref.read().last_alert_type
        return lastAlertType?.toAlertType() ?: TripPositionAlertType.NULL
    }
}