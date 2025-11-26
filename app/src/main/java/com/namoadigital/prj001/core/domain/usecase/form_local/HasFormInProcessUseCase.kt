package com.namoadigital.prj001.core.domain.usecase.form_local

import com.namoa_digital.namoa_library.util.ConstantBase.SYS_STATUS_IN_PROCESSING
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import javax.inject.Inject

class HasFormInProcessUseCase @Inject constructor(
    private val repository: GeCustomFormRepository
) : UseCaseWithoutFlow<Unit, List<GE_Custom_Form_Local>> {

    override fun invoke(input: Unit): List<GE_Custom_Form_Local> {
        return repository.getFormByStatus(SYS_STATUS_IN_PROCESSING)
    }

}