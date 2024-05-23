package com.namoadigital.prj001.core.trip.domain.usecase.destination

import android.os.Bundle
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.domain.usecase.destination.SelectDestinationUseCase.SelectDestinationParam

class SelectDestinationUseCase(
    private val repository: TripDestinationRepository,
) : UseCaseWithoutFlow<SelectDestinationParam, Unit> {
    override fun invoke(input: SelectDestinationParam) {
        Bundle().apply {
            this.putString(DESTINATION_TYPE, input.destinationType)
            this.putInt(TRIP_PREFIX, input.tripPrefix)
            this.putInt(TRIP_CODE, input.tripCode)
            this.putInt(TRIP_SCN, input.tripScn)

            input.siteCode?.let{
                this.putInt(SITE_CODE,it)
            }

            input.destinationTicketPrefix?.let{
                this.putInt(DESTINATION_TICKET_PREFIX,it)
            }

            input.destinationTicketCode?.let{
                this.putInt(DESTINATION_TICKET_CODE,it)
            }

            input.currentLat?.let{
                this.putDouble(CURRENT_LAT,it)
            }

            input.currentLon?.let{
                this.putDouble(CURRENT_LON,it)
            }
        }.let { bundle ->
            repository.execServiceSelectDestination(bundle)
        }
    }

    data class SelectDestinationParam(
        val tripPrefix: Int,
        val tripCode: Int,
        val tripScn: Int,
        val destinationType: String,
        val siteCode: Int?=null,
        val destinationTicketPrefix: Int?=null,
        val destinationTicketCode: Int?=null,
        val currentLat: Double?=null,
        val currentLon: Double?=null,
    )


    companion object {

        const val DESTINATION_TYPE = "DESTINATION_TYPE"
        const val SITE_CODE = "SITE_CODE"
        const val TRIP_PREFIX = "TRIP_PREFIX"
        const val TRIP_CODE = "TRIP_CODE"
        const val TRIP_SCN = "TRIP_SCN"
        const val DESTINATION_TICKET_PREFIX = "DESTINATION_TICKET_PREFIX"
        const val DESTINATION_TICKET_CODE = "DESTINATION_TICKET_CODE"
        const val CURRENT_LAT = "CURRENT_LAT"
        const val CURRENT_LON = "CURRENT_LON"

    }
}
