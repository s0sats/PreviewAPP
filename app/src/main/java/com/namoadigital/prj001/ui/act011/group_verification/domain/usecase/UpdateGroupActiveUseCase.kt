package com.namoadigital.prj001.ui.act011.group_verification.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroup
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateGroupActiveUseCase @Inject constructor(
    private val repository: GeOsRepository,
) : UseCases<UpdateGroupActiveUseCase.Input, GeOsVg?> {

    data class Input(
        val customerCode: Long,
        val customFormType: Int,
        val customFormCode: Int,
        val customFormVersion: Int,
        val customFormData: Int,
        val productCode: Long,
        val serialCode: Long,
        val isContinuousForm: Boolean,
        val vgCode: Int,
        val isActive: Boolean,
    )

    override suspend fun invoke(input: Input): Flow<IResult<GeOsVg>> {
        return repository.updateActiveFromGeOsVg(
            customerCode = input.customerCode,
            customFormType = input.customFormType,
            customFormCode = input.customFormCode,
            customFormVersion = input.customFormVersion,
            customFormData = input.customFormData,
            productCode = input.productCode,
            serialCode = input.serialCode,
            vgCode = input.vgCode,
            isActive = if(input.isActive) 1 else 0,
            isContinuousForm = input.isContinuousForm,
        )
    }

}
