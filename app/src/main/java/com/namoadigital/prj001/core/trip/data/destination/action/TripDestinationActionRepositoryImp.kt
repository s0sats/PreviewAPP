package com.namoadigital.prj001.core.trip.data.destination.action

import android.content.Context
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.core.trip.data.destination.action.mapping.toTripForms

import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationActionDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.ui.act005.trip.repository.mapping.toActionExtract
import com.namoadigital.prj001.ui.act095.event_manual.presentation.dialog.domain.model.EventConflict
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TripDestinationActionRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tripDao: FSTripDao,
    private val dao: FsTripDestinationActionDao,
    private val formDao: GE_Custom_Form_LocalDao
) : TripDestinationActionRepository {

    override fun getDestinationActions(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<FsTripDestinationAction> {
        return dao.getListDestinationAction(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq
        )
    }

    override fun getDestinationActionExtract(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<TripSiteExtract<FsTripDestinationAction>> {
        return dao.getListDestinationAction(
            customerCode,
            tripPrefix,
            tripCode,
            destinationSeq
        ).map {
            it.toTripForms()
        }
    }

    override fun getFormInProcessExtract(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int
    ): List<TripSiteExtract<GE_Custom_Form_Local>> {
        return formDao.getFormInProcessFromTrip(customerCode, tripPrefix, tripCode, destinationSeq)
            .map {
                it.toTripForms()
            }
    }

    override fun checkDateConflict(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int?,
        newStart: String,
        newEnd: String?,
        validateStartDateEquals: Boolean
    ): EventConflict? {

        return dao.getDestinationActionConflict(
            customerCode = context.getCustomerCode(),
            tripPrefix,
            tripCode,
            destinationSeq ?: -1,
            newStart = newStart,
            newEnd = newEnd,
            validateStartDateEquals = validateStartDateEquals
        )
    }

    override fun getDestinationFormDateConflict(
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        newArrivedDate: String,
        newDepartedDate: String?,
    ): EventConflict? {
        return dao.getDestinationFormDateConflict(
            customerCode = context.getCustomerCode(),
            tripPrefix,
            tripCode,
            destinationSeq,
            newArrivedDate,
            newDepartedDate
        )
    }

    override fun getExtract(trip: FSTrip?): List<Extract<FsTripDestinationAction>> {
        trip?.let { trip ->
            return dao.getExtract(trip.tripPrefix, trip.tripCode)
                .map { it.toActionExtract() }
        }

        return emptyList()
    }
}