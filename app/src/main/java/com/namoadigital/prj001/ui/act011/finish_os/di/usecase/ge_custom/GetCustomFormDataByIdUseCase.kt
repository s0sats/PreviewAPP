package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomFormDataByIdUseCase @Inject constructor(
    private val repository: GeCustomFormRepository,
) : UseCases<GetCustomFormDataByIdUseCase.Param, GE_Custom_Form_Data?> {

    override suspend fun invoke(input: Param): Flow<IResult<GE_Custom_Form_Data?>> {
        return repository.getCustomFormDataById(
            input.formTypeCode,
            input.formCode,
            input.formVersionCode,
            input.formData
        )
    }


    data class Param(
        val formTypeCode: Int,
        val formCode: Int,
        val formVersionCode: Int,
        val formData: Long
    )

}