package com.namoadigital.prj001.ui.act005.trip.di.usecase.user

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository

class ExecEditUserUseCase constructor(
    private val repository: TripUserRepository
) : UseCaseWithoutFlow<ExecEditUserUseCase.ExecEditUserParams,Unit>{

    data class ExecEditUserParams(
        val user: TripUserEdit,
        val action: UserAction
    )

    override fun invoke(input: ExecEditUserParams) {
        repository.execSaveUsers(input.user, input.action)
    }

}