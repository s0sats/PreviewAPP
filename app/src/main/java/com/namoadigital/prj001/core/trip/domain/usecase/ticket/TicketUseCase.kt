package com.namoadigital.prj001.core.trip.domain.usecase.ticket

data class TicketUseCase(
    val get: GetTicketByIdUseCase,
    val download: DownloadTicketUseCase,
    val ticketForm: GetTicketFormByIdUseCase
)