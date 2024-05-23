package com.namoadigital.prj001.core.trip.domain.usecase.destination.action

data class DestinationActionUseCase(
    val execGetDestinationAction: GetDestinationActionUseCase,
    val listExecutionsUseCase: GetListSiteExtractUseCase
)