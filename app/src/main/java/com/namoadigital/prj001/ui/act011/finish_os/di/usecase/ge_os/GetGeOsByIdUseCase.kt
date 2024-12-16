package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGeOsByIdUseCase @Inject constructor(
    private val repository: GeOsRepository
) : UseCases<GetGeOsByIdUseCase.Param, GeOs?> {

    override suspend fun invoke(input: Param): Flow<IResult<GeOs?>> {
        return repository.getGeOsById(
            formType = input.formType,
            formCode = input.formCode,
            formVersion = input.formVersion,
            formData = input.formData,
        )
    }


    data class Param(
        val formType: Int,
        val formCode: Int,
        val formVersion: Int,
        val formData: Long,
    )

}
