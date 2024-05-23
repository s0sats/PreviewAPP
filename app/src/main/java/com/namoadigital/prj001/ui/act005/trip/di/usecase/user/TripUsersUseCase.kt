package com.namoadigital.prj001.ui.act005.trip.di.usecase.user

data class TripUsersUseCase(
    val getUsers: GetListTechnicalUseCase,
    val edit: ExecEditUserUseCase,
    val intersection: UserCheckIntersectionUseCase
)
