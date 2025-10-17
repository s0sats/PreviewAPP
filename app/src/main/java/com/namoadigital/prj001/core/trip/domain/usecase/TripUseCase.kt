package com.namoadigital.prj001.core.trip.domain.usecase

import com.namoadigital.prj001.ui.act005.trip.di.usecase.start_trip.SaveStartDateTripUseCase

data class TripUseCase(
    val trip: GetTripUseCase,
    val statusChange: TripStatusChangeUseCase,
    val savePrefTrip: SavePreferenceTripUseCase,
    val saveFleet: SaveFleetDataUseCase,
    val listSites: ListSitesUseCase,
    val saveOrigin: SaveOriginUseCase,
    val getEvent: GetEventUseCase,
    val sendTripFull: SendTripFullUseCase,
    val hasTripWithUpdateRequired: CheckExistsTripUpdateUseCase,
    val saveStartDate: SaveStartDateTripUseCase,
)
