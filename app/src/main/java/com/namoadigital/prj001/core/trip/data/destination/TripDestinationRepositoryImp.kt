package com.namoadigital.prj001.core.trip.data.destination

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.core.trip.domain.model.OdometerArrivedDestination
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.putApiRequest
import com.namoadigital.prj001.model.location.Coordinates
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.TripDestinationEditEnv
import com.namoadigital.prj001.model.trip.TripDestinationStatusChangeEnv
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.receiver.trip.WBRDestinationEdit
import com.namoadigital.prj001.receiver.trip.WBR_AvailablesDestinations
import com.namoadigital.prj001.receiver.trip.WBR_SelectDestination
import com.namoadigital.prj001.receiver.trip.WBR_TripDestinationStatusChange
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toExtract
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.AvailableDestinationFilter
import com.namoadigital.prj001.ui.act094.destination.domain.select_destination.SelectDestinationRec
import com.namoadigital.prj001.ui.act094.destination.local.preference.DestinationFilterPreference
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import javax.inject.Inject

class TripDestinationRepositoryImp @Inject constructor(
    private val context: Context,
    private val dao: FsTripDestinationDao,
    private val tripDao: FSTripDao? = null,
) : TripDestinationRepository {

    private val preference: DestinationFilterPreference = DestinationFilterPreference.instance(context)
    override fun execServiceAvailableDestination(bundle: Bundle) {
        Intent(context, WBR_AvailablesDestinations::class.java).apply {
            putExtras(bundle)
            context.sendBroadcast(this)
        }
    }

    override fun execServiceSelectDestination(bundle: Bundle) {
        Intent(context, WBR_SelectDestination::class.java).apply {
            putExtras(bundle)
            context.sendBroadcast(this)
        }
    }

    override fun saveFilterPreference(filter: AvailableDestinationFilter) {
        preference.write(filter)
    }

    override fun getDestinationFilterPreference(): AvailableDestinationFilter {
        return preference.read()
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FsTripDestination>>{
        trip?.let { trip ->
            return  dao.getExtract(trip.tripPrefix, trip.tripCode).map { it.toExtract() }
        }

        return emptyList()
    }

    override fun getListOdometerArrived(): List<OdometerArrivedDestination> {
        tripDao?.getTrip()?.let { trip ->
            return dao.getListOdometer(trip.tripPrefix, trip.tripCode)
        }
        return emptyList()
    }


    override fun getDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): FsTripDestination? {
        return dao.getDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
        )
    }


    override fun getTripDestinations(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int
    ): List<FsTripDestination> {
        return dao.getAllDestination(
            customerCode,
            tripPrefix,
            tripCode
        )
    }

    override fun getTripLastDestinationCoordinate(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int
    ): Coordinates? {
        //
        val tripDestinations = getTripDestinations(
            customerCode,
            tripPrefix,
            tripCode
        )
        //
        if (tripDestinations.isEmpty()) {
            return null
        }
        //
        return tripDestinations.last().coordinates
    }

    override fun getDestinationByStatus(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        status: DestinationStatus
    ): FsTripDestination? {
        return dao.getDestinationByStatus(customerCode, tripPrefix, tripCode, status.name)
    }

    override fun getDestinationStatus(
        tripPrefix: Int,
        tripCode: Int,
    ): DestinationStatus {
        return dao.getDestinationStatus(
            customerCode = context.getCustomerCode(),
            tripPrefix,
            tripCode
        )?.toDestinationStatus() ?: DestinationStatus.NULL
    }

    override fun getCoordinatesDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
    ): Coordinates? {
        val destination = getDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
        )
        return destination?.coordinates
    }

    override fun saveDestination(
        customerCode: Long,
        response: String?,
        destination: SelectionDestinationAvailable
    ): Boolean {
        val gson = GsonBuilder().serializeNulls().create()
        val remoteDestination = gson.fromJson(response, SelectDestinationRec::class.java)
        val fsTripDestination = FsTripDestination(
            customerCode,
            remoteDestination.tripPrefix,
            remoteDestination.tripCode,
            remoteDestination.destinationSeq,
            destination.destinationType,
            destination.siteCode,
            destination.siteDesc,
            destination.regionCode,
            destination.regionDesc,
            destination.ticketPrefix,
            destination.ticketCode,
            "",
            remoteDestination.destinationStatus,
            remoteDestination.lat,
            remoteDestination.lon,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            destination.countryId,
            destination.state,
            destination.city,
            destination.district,
            destination.street,
            destination.streetnumber,
            destination.complement,
            destination.zipCode,
            destination.plusCode,
            destination.contactName,
            destination.contactPhone,
            destination.siteMainUser,
            destination.minDate,
            destination.serialCnt ?: 0,
        )
        //
        return tripDao?.let {
            it.addTripDestination(
                fsTripDestination,
                remoteDestination.scn,
                remoteDestination.tripStatus
            )
        } ?: false
        //
    }
    override fun saveOverNightDestination(
        customerCode: Long,
        remoteDestination: SelectDestinationRec,
    ): Boolean {
        

        val fsTripDestination = FsTripDestination(
            customerCode,
            remoteDestination.tripPrefix,
            remoteDestination.tripCode,
            remoteDestination.destinationSeq,
            FsTripDestination.OVER_NIGHT_DESTINATION_TYPE,
            null,
            null,
            null,
            null,
            null,
            null,
            "",
            remoteDestination.destinationStatus,
            remoteDestination.lat,
            remoteDestination.lon,
            remoteDestination.arrivedDate,
            remoteDestination.lat,
            remoteDestination.lon,
            TripDestinationStatusChangeEnv.TYPE_MANUAL,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            0,
        )
        //
        return tripDao?.let {
            it.addTripDestination(
                fsTripDestination,
                remoteDestination.scn,
                remoteDestination.tripStatus
            )
        } ?: false
        //
    }

    override fun setTripDestinationStatusChange(modelRequest: TripDestinationStatusChangeEnv) {
        context.sendToWebServiceReceiver<WBR_TripDestinationStatusChange> {
            Bundle().putApiRequest(modelRequest)
        }
    }

    override fun saveDestinationDate(
        dateStart: String,
        dateEnd: String,
        destinationSeq: Int
    ) {
        tripDao?.getTrip()?.let { trip ->

            TripDestinationEditEnv(
                tripPrefix = trip.tripPrefix,
                tripCode = trip.tripCode,
                scn = trip.scn,
                destinationSeq = destinationSeq,
                arrivedDate = dateStart,
                departedDate = dateEnd
            ).let { model ->
                context.sendToWebServiceReceiver<WBRDestinationEdit> {
                    Bundle().putApiRequest(model)
                }
            }

        }
    }

    override fun getPreviousValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination? {
        return dao.previousDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
            type,
        )
    }

    override fun getNextValidDestination(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType
    ): FsTripDestination? {
        return dao.nextDestination(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq,
            type,
        )
    }
}