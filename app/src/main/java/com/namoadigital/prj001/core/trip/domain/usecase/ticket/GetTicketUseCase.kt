package com.namoadigital.prj001.core.trip.domain.usecase.ticket

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.model.TK_Ticket

class GetTicketUseCase constructor(
    private val repository: TicketRepository
): UseCaseWithoutFlow<GetTicketUseCase.GetTicketParams, TK_Ticket?>{

    data class GetTicketParams(
        val prefix: Int,
        val code: Int,
    )

    override fun invoke(input: GetTicketParams): TK_Ticket? {
        return repository.getTicket(input.prefix, input.code)
    }
}