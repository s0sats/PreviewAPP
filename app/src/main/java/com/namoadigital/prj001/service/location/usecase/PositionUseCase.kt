package com.namoadigital.prj001.service.location.usecase

data class PositionUseCase(
    val checkStatusLocation: CheckStatusLocationUseCase,
    val save: PositionSaveUseCase,
    val execLastPositionPackage: ExecLastPositionPackageUseCase,
    val positionCalculateDistanceAndSaveUseCase: PositionCalculateDistanceAndSaveUseCase,
    val positionDefinedDestinationUseCase: PositionDefinedDestinationUseCase,
    val positionUndefinedDestinationUseCase: PositionUndefinedDestinationUseCase
)