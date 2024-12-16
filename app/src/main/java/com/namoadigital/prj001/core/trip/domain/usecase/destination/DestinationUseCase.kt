package com.namoadigital.prj001.core.trip.domain.usecase.destination

import android.content.Context
import com.namoadigital.prj001.core.data.local.repository.serial.ProductSerialRepositoryImp
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepositoryImp
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepositoryImp
import com.namoadigital.prj001.core.trip.data.trip.TripRepositoryImp
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SaveDestinationDateUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.SetDestinationStatusUseCase
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.dao.trip.FsTripDestinationDao

data class DestinationUseCase(
    val execGetAvailableDestination: GetAvailablesDestinationsUseCase? = null,
    val execSelectDestination: SelectDestinationUseCase? = null,
    val getFilterPreference: GetFilterPreferenceUseCase? = null,
    val saveFilterPreference: SaveDestinationFilterUseCase? = null,
    val destination: GetDestinationUseCase? = null,
    val destinationByStatus: GetDestinationByStatusUseCase? = null,
    val saveDestination: SaveDestinationUseCase? = null,
    val getDestinationCounter: GetDestinationCounter? = null,
    val setDestinationStatusUseCase: SetDestinationStatusUseCase? = null,
    val listOdometerArrived: ListOdometerArrivedUseCase? = null,
    val saveDestinationDateUseCase: SaveDestinationDateUseCase? = null,
    val getDestinationForThresholdValidationUseCase: GetDestinationForThresholdValidationUseCase? = null,
    val saveOverNightDestinationUseCase: SaveOverNightDestinationUseCase? = null
) {


    companion object INSTANCE {

        fun selectDestinationUseCase(context: Context): DestinationUseCase {
            val tripDao = FSTripDao(context)
            val tripDestinationRepository = TripDestinationRepositoryImp(
                context,
                FsTripDestinationDao(context),
                tripDao
            )

            val tripRepository = TripRepositoryImp(context, tripDao)

            return DestinationUseCase(
                GetAvailablesDestinationsUseCase(
                    tripDestinationRepository,
                    TicketRepositoryImp(context, TK_TicketDao(context)),
                    ProductSerialRepositoryImp(context, MD_Product_SerialDao(context))),
                SelectDestinationUseCase(tripDestinationRepository),
                GetFilterPreferenceUseCase(tripDestinationRepository),
                SaveDestinationFilterUseCase(tripDestinationRepository),
                saveDestination = SaveDestinationUseCase(tripDestinationRepository, tripRepository, CheckNextStatusWhenNewDestinationUseCase(tripDestinationRepository, tripRepository)),
                getDestinationForThresholdValidationUseCase = GetDestinationForThresholdValidationUseCase(tripDestinationRepository),
            )
        }

    }


}