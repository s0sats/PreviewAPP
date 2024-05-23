package com.namoadigital.prj001.service.location.usecase.module

import com.namoadigital.prj001.core.trip.data.destination.TripDestinationRepository
import com.namoadigital.prj001.core.trip.data.position.TripPositionRepository
import com.namoadigital.prj001.core.trip.data.preference.CurrentTripPref
import com.namoadigital.prj001.core.trip.data.trip.TripRepository
import com.namoadigital.prj001.service.location.usecase.CheckStatusLocationUseCase
import com.namoadigital.prj001.service.location.usecase.ExecLastPositionPackageUseCase
import com.namoadigital.prj001.service.location.usecase.PositionCalculateDistanceAndSaveUseCase
import com.namoadigital.prj001.service.location.usecase.PositionDefinedDestinationUseCase
import com.namoadigital.prj001.service.location.usecase.PositionSaveUseCase
import com.namoadigital.prj001.service.location.usecase.PositionUndefinedDestinationUseCase
import com.namoadigital.prj001.service.location.usecase.PositionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object Module {

    @ServiceScoped
    @Provides
    fun providesPositionUseCase(
        tripPositionRepository: TripPositionRepository,
        tripDestinationRepository: TripDestinationRepository,
        positionRepository: TripPositionRepository,
        tripRepository: TripRepository,
        currentTripPref: CurrentTripPref
    ): PositionUseCase {
        val positionSaveUseCase = PositionSaveUseCase(
            tripPositionRepository,
            tripRepository,
            tripDestinationRepository
        )
        val calculateDistanceAndSaveUseCase = PositionCalculateDistanceAndSaveUseCase(
            tripRepository,
            tripPositionRepository,
            positionSaveUseCase,
            currentTripPref
        )
        val positionDefinedDestinationUseCase = PositionDefinedDestinationUseCase(
            tripRepository,
            tripPositionRepository,
            tripDestinationRepository,
            positionSaveUseCase,
            currentTripPref
        )
        val positionUndefinedDestinationUseCase =
            PositionUndefinedDestinationUseCase(
                tripRepository,
                tripDestinationRepository,
                positionRepository,
                positionSaveUseCase,
                calculateDistanceAndSaveUseCase,
                currentTripPref
            )
        return PositionUseCase(
            checkStatusLocation = CheckStatusLocationUseCase(
                tripRepository,
                tripDestinationRepository,
                positionUndefinedDestinationUseCase,
                positionDefinedDestinationUseCase,
                currentTripPref
            ),
            save = positionSaveUseCase,
            positionDefinedDestinationUseCase = positionDefinedDestinationUseCase,
            positionUndefinedDestinationUseCase = positionUndefinedDestinationUseCase,
            positionCalculateDistanceAndSaveUseCase = calculateDistanceAndSaveUseCase,
            execLastPositionPackage = ExecLastPositionPackageUseCase(currentTripPref = currentTripPref, positionRepository)
        )
    }

}
