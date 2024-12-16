package com.namoadigital.prj001.ui.act005.trip.fragment.onsite

import androidx.lifecycle.ViewModel
import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.core.trip.domain.usecase.GetDestinationByStatusUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.DestinationUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.destination.action.DestinationActionUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.DownloadTicketUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.GetTicketByIdUseCase
import com.namoadigital.prj001.core.trip.domain.usecase.ticket.TicketUseCase
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.trip.DestinationStatus
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FsTripDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripOnSiteViewModel @Inject constructor(
    private val destinationUseCase: DestinationUseCase,
    private val destinationActionUseCase: DestinationActionUseCase,
   private val ticketUseCase: TicketUseCase
): ViewModel() {

    fun getDestinationAction(destination: FsTripDestination): List<TripSiteExtract<*>> {
        return destinationActionUseCase.listExecutionsUseCase(destination)
    }
    fun getNextDestination(customerCode: Long, trip: FSTrip):FsTripDestination?{
        return destinationUseCase.destinationByStatus?.invoke(
            GetDestinationByStatusUseCase.GetDestinationParams(
                customerCode,
                trip.tripPrefix,
                trip.tripCode,
                DestinationStatus.PENDING
            )
        )
    }
    //
    fun checkDepartedAvailability(destination: FsTripDestination, dataRequired: Boolean): Boolean {
        return destination.arrivedFleetOdometer != null
                && (!destination.arrivedFleetPhoto.isNullOrBlank()
                || !destination.arrivedFleetPhotoName.isNullOrBlank()
                ) || !dataRequired
    }

    fun getTicket(
        prefix: Int,
        code: Int,
    ): TK_Ticket? {
        return ticketUseCase.get(GetTicketByIdUseCase.GetTicketParams(prefix, code))
    }

    fun downloadTicket(
        prefix: Int,
        code: Int
    ){
        ticketUseCase.download(DownloadTicketUseCase.DownloadTicketParams(prefix, code))
    }
}