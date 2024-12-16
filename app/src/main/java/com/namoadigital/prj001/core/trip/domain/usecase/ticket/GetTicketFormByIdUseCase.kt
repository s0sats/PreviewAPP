package com.namoadigital.prj001.core.trip.domain.usecase.ticket

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.model.TK_Ticket_Form

class GetTicketFormByIdUseCase constructor(
    private val repository: TicketRepository
): UseCaseWithoutFlow<GetTicketFormByIdUseCase.Param, TK_Ticket_Form?>{

    data class Param(
        val ticketPrefix: Int,
        val ticketCode: Int,
        val stepCode: Int,
        val ticketSeqTmp: Int,
    )

    override fun invoke(input: Param): TK_Ticket_Form? {
        return repository.getTicketFormById(
            input.ticketPrefix,
            input.ticketCode,
            input.stepCode,
            input.ticketSeqTmp
        )
    }

}
