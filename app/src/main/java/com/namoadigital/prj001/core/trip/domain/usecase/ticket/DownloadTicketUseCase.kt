package com.namoadigital.prj001.core.trip.domain.usecase.ticket

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository

class DownloadTicketUseCase constructor(
    private val repository: TicketRepository
): UseCaseWithoutFlow<DownloadTicketUseCase.DownloadTicketParams, Unit>{

    data class DownloadTicketParams(
        val prefix: Int,
        val code: Int
    )

    override fun invoke(input: DownloadTicketParams) {
        val (prefix, code) = input
        repository.downloadTicket(prefix, code)
    }

}
