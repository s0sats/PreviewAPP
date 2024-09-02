package com.namoadigital.prj001.ui.act005.trip.di.usecase.user

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import kotlinx.coroutines.flow.Flow

class ExecEditUserUseCase constructor(
    private val repository: TripUserRepository
) : UseCases<ExecEditUserUseCase.ExecEditUserParams, Unit> {

    data class ExecEditUserParams(
        val user: TripUserEdit,
        val action: UserAction
    )

    override suspend fun invoke(input: ExecEditUserParams): Flow<IResult<Unit>> {
        return repository.execSaveUsers(input.user, input.action)
    }

}