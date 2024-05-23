package com.namoadigital.prj001.core.trip.domain.usecase

data class TripUseCase(
    val trip: GetTripUseCase,
    val statusChange: TripStatusChangeUseCase,
    val savePrefTrip: SavePreferenceTripUseCase,
    val saveFleet: SaveFleetDataUseCase,
    val listSites: ListSitesUseCase,
    val saveOrigin: SaveOriginUseCase,
    val getEvent: GetEventUseCase,
)
