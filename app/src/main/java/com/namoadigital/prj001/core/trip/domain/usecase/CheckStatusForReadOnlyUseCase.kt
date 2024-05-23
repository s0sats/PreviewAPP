package com.namoadigital.prj001.core.trip.domain.usecase

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.dao.EV_User_CustomerDao
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toDestinationStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.sql.EV_User_Customer_Sql_002
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class CheckStatusForReadOnlyUseCase constructor(
    private val app: Context,
    private val repository: TripRepository,
    private val destinationRepository: TripDestinationRepository,
): UseCaseWithoutFlow<String, Boolean> {

    override fun invoke(input: String): Boolean {
        val isUserOnFieldServiceModeOnly = isUserOnFieldServiceModeOnly()
        val trip = repository.getTrip() ?: return isUserOnFieldServiceModeOnly
        val tripStatus = trip.tripStatus.toTripStatus()
        val destination =
            destinationRepository.getDestinationByStatus(
                trip.customerCode,
                trip.tripPrefix,
                trip.tripCode,
                DestinationStatus.ARRIVED
            )
        val destinationStatusArrived = destination?.let{
            it.destinationStatus?.toDestinationStatus() == DestinationStatus.ARRIVED
        }?: false

        return when (input) {
            ConstantBaseApp.ACT068, ConstantBaseApp.ACT094 -> true
            ConstantBaseApp.ACT005 -> !(checkTripStatus(tripStatus) && destinationStatusArrived)
            else -> false
        }
    }

    private fun isUserOnFieldServiceModeOnly(): Boolean {
        val userCustomer = EV_User_CustomerDao(
            app,
            Constant.DB_FULL_BASE,
            Constant.DB_VERSION_BASE
        ).getByString(
            EV_User_Customer_Sql_002(
                ToolBox_Con.getPreference_User_Code(app),
                ToolBox_Con.getPreference_Customer_Code(app).toString()
            ).toSqlQuery()
        )
        return userCustomer != null && userCustomer.field_service_mode_only == 1
    }


    private fun checkDestinationStatus(destinationStatus: DestinationStatus) = destinationStatus == DestinationStatus.ARRIVED
    private fun checkTripStatus(tripStatus: TripStatus) = tripStatus == TripStatus.ON_SITE || tripStatus == TripStatus.NULL
}