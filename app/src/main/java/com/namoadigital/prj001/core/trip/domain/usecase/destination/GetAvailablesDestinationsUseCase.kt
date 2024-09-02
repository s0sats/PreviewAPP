package com.namoadigital.prj001.core.trip.domain.usecase.destination

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.data.local.repository.serial.SerialRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.ui.act094.destination.domain.destination_availables.DestinationAvailables
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAvailablesDestinationsUseCase constructor(
    private val repository: TripDestinationRepository,
    private val ticketRepository: TicketRepository,
    private val serialRepository: SerialRepository,
) : UseCases<Unit, List<DestinationAvailables>> {

    private fun booleanToInt(value: Boolean) = when (value) {
        true -> 1
        false -> 0
    }


    override suspend fun invoke(input: Unit): Flow<IResult<List<DestinationAvailables>>> {
        return flow {
            emit(loading(true))
            emit(success(getListDestinationAvailable().sortedBy { ToolBox_Inf.dateToMilliseconds(it.minDate) }))
        }
    }

    private suspend fun getListDestinationAvailable(): List<DestinationAvailables> {

        val list = mutableListOf<DestinationAvailables>()

        repository.getListExternalAddress().let(list::addAll)
        repository.getListSiteAddress().map { destination ->
            destination.copy(
                priorityCnt = ticketRepository.getTicketPriorityCntList(destination.siteCode ?: -1),
                todayCnt = ticketRepository.getTicketTodayCntList(destination.siteCode ?: -1),
                lateCnt = ticketRepository.getTicketLateCntList(destination.siteCode ?: -1),
                nextCnt = ticketRepository.getTicketNextList(destination.siteCode ?: -1),
                serialCnt = serialRepository.getListSerialsBySiteCode(destination.siteCode ?: -1).size
            )
        }.let(list::addAll)

        return list

    }

    companion object {

        const val SHOW_ONLY_TODAY = "show_only_today"
        const val SHOW_ONLY_PRIORITY = "show_only_priority"
        const val HIDE_PREVENTIVE = "hide_preventive"
        const val SHOW_ONLY_SITE_WITH_PLANNING = "show_only_site_with_planning"
        const val SITE_CODE = "site_code"

    }

}