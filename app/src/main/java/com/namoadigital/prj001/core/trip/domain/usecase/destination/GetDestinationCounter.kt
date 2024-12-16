package com.namoadigital.prj001.core.trip.domain.usecase.destination

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.serial.ProductSerialRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.model.trip.DestinationCounter
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.util.ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.concurrent.TimeUnit

class GetDestinationCounter constructor(
    private val context: Context,
    private val destinationRepository: TripDestinationRepository,
    private val ticketRepository: TicketRepository,
    private val ticketCacheRepository: TicketCacheRepository,
    private val productSerialRepository: ProductSerialRepository,
) : UseCaseWithoutFlow<FsTripDestination, DestinationCounter> {

    override fun invoke(input: FsTripDestination): DestinationCounter {
        var priorityCnt = 0
        var todayCnt = 0
        var lateCnt = 0
        var next = 0
        var serial = 0

        when {
            input.isTicket -> {
                val ticketNormal = ticketRepository.getTicketKanban(
                    input.ticketPrefix ?: -1,
                    input.ticketCode ?: -1
                )
                val ticketCache = ticketCacheRepository.getTicketKanban(
                    input.ticketPrefix ?: -1,
                    input.ticketCode ?: -1
                )

                val ticket = ticketNormal ?: ticketCache

                ticket?.let { tk ->
                    countersFromTicket(
                        ticket = tk,
                        daysBetween = { isPriority, millis ->
                            if (isPriority) priorityCnt = 1
                            when {
                                millis > 0 && millis <= TimeUnit.DAYS.toMillis(7) -> next = 1
                                millis == 0L -> todayCnt = 1
                                millis < 0L -> lateCnt = 1
                            }
                        }
                    )
                }

            }

            else -> {
                //repositorio de destination trip
                val destination = destinationRepository.getDestination(
                    input.customerCode,
                    input.tripPrefix,
                    input.tripCode,
                    input.destinationSeq,
                )
                input.destinationSiteCode?.let {
                    priorityCnt += ticketRepository.getTicketPriorityCntList(it)
                    todayCnt += ticketRepository.getTicketTodayCntList(it)
                    lateCnt += ticketRepository.getTicketLateCntList(it)
                    next += ticketRepository.getTicketNextList(it)
                    //
                    priorityCnt += ticketCacheRepository.getPriorityCntList(it)
                    todayCnt += ticketCacheRepository.getTodayCntList(it)
                    lateCnt += ticketCacheRepository.getLateCntList(it)
                    next += ticketCacheRepository.getNextList(it)
                    serial += productSerialRepository.getListSerialsBySiteCode(it).size
                }
                //
                //
            }
        }
        return DestinationCounter(
            formatCounter(priorityCnt),
            formatCounter(todayCnt),
            formatCounter(lateCnt),
            formatCounter(next),
            formatCounter(serial),
        )
    }

    private fun formatCounter(counter: Int): String {
        return if (counter > 0) counter.toString() else "0"
    }


    private fun countersFromTicket(ticket: Any, daysBetween: (Boolean, Long) -> Unit) {
        val today = ToolBox.dateToMilliseconds(FULL_TIMESTAMP_TZ_FORMAT)
        when (ticket) {
            is TK_Ticket -> {
                val dateMillis = checkTicketDate(ticket.kanban_date, today)
                daysBetween(ticket.is_priority == 1, dateMillis)
            }

            is TkTicketCache -> {
                val dateMillis = checkTicketDate(ticket.forecast_start, today)
                daysBetween(ticket.is_priority == 1, dateMillis)
            }

        }
    }

    private fun checkTicketDate(ticketDate: String?, today: Long): Long {
        val dateMillis = ticketDate?.let{
            if (ToolBox_Inf.checkSameDayDate(
                    ticketDate,
                    ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
                )
            ) {
                0
            } else {
                val tkDate = ToolBox.dateToMilliseconds(ticketDate)
                tkDate - today
            }
        }?: Long.MAX_VALUE

        return dateMillis
    }


}