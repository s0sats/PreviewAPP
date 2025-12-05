package com.namoadigital.prj001.core.data.domain.usecase.ticket

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.serial.SerialRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository

class ProcessDownloadTicketUseCase  constructor(
    private val repository: TicketRepository,
    private val serialRepository: SerialRepository,
): UseCaseWithoutFlow<ProcessDownloadTicketUseCase.Input, ProcessDownloadTicketUseCase.Output> {
    class Input {

    }

    class Output {

    }


    override fun invoke(input: ProcessDownloadTicketUseCase.Input): ProcessDownloadTicketUseCase.Output {
        TODO("Not yet implemented")
    }


}