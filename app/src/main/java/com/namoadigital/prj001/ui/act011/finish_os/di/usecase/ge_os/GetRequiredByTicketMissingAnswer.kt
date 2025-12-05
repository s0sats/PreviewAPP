package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_os

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_os.GeOsRepository
import javax.inject.Inject

class GetRequiredByTicketMissingAnswer @Inject constructor(
    private val repository: GeOsRepository
) : UseCaseWithoutFlow<GetRequiredByTicketMissingAnswer.Input, Int> {
    override fun invoke(input: Input): Int {
        return if(
            input.ticketPrefix != null
            && input.ticketCode != null
        ){
            repository.getDeviceItemRequiredByTicketMissingAnswer(
                input.formType,
                input.formCode,
                input.formVersion,
                input.formData,
                input.ticketPrefix,
                input.ticketCode
            )
        }else{
            0
        }
    }


    data class Input(
        val formType: Int,
        val formCode: Int,
        val formVersion: Int,
        val formData: Long,
        val ticketPrefix: Int?,
        val ticketCode: Int?,
    )


}