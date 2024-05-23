package com.namoadigital.prj001.ui.act005.trip.di.usecase.user

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository

class GetListTechnicalUseCase(
    private val repository: TripUserRepository
) : UseCaseWithoutFlow<Unit, Unit> {
    override fun invoke(input: Unit) {
        repository.execGetListUsers()
    }

}
