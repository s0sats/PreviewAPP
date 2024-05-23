package com.namoadigital.prj001.core.trip.domain.usecase.ticket

data class TicketUseCase(
    val get: GetTicketUseCase,
    val download: DownloadTicketUseCase
)