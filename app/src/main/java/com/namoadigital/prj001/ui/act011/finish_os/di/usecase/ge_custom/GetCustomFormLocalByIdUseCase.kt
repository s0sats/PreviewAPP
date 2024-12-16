package com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomFormLocalByIdUseCase @Inject constructor(
    private val repository: GeCustomFormRepository
) : UseCases<GetCustomFormLocalByIdUseCase.Param, GE_Custom_Form_Local?> {

    override suspend fun invoke(input: Param): Flow<IResult<GE_Custom_Form_Local?>> {
        if (input.ticket != null && input.ticket.isValidTicket()) {
            return repository.getCustomFormLocalTicketById(
                formTypeCode = input.formTypeCode,
                formCode = input.formCode.toString(),
                formVersionCode = input.formVersionCode.toString(),
                formData = input.formData.toString(),
                productCode = input.productCode,
                serialId = input.serialId,
                ticketPrefix = input.ticket.ticketPrefix!!,
                ticketCode = input.ticket.ticketCode!!,
                ticketSeq = input.ticket.ticketSeq!!,
                ticketSeqTmp = input.ticket.ticketSeqTmp!!,
                stepCode = input.ticket.stepCode!!
            )
        }

        return repository.getGeCustomFormLocalById(
            formTypeCode = input.formTypeCode,
            formCode = input.formCode.toString(),
            formVersionCode = input.formVersionCode.toString(),
            formData = input.formData.toString(),
            productCode = input.productCode,
            serialId = input.serialId
        )
    }


    data class Param(
        val formCode: Int,
        val formTypeCode: Int,
        val formVersionCode: Int,
        val formData: Long,
        val productCode: Int,
        val serialId: String,
        val ticket: TicketParam? = null
    ) {
        data class TicketParam(
            val ticketPrefix: Int? = null,
            val ticketCode: Int? = null,
            val ticketSeq: Int? = null,
            val ticketSeqTmp: Int? = null,
            val stepCode: Int? = null
        ){
            fun isValidTicket() : Boolean {
                return listOf(
                    ticketPrefix,
                    ticketCode,
                    ticketSeq,
                    ticketSeqTmp,
                    stepCode
                ).any { it != null }
            }
        }
    }
}
