package com.namoadigital.prj001.core.trip.domain.usecase

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketRepositoryImp
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.ui.base.NamoaFactory

class GetTicketActionUseCase constructor(
    private val repository: TicketRepository,
): UseCaseWithoutFlow<GetTicketActionUseCase.Params, GetTicketActionUseCase.Results> {

    override fun invoke(input: Params): Results {
        val tickets = mutableListOf<HMAux>()

        val focus = if(input.isFocus) 1 else 0

        tickets.addAll(
            repository.getTicketActionList(
                input.siteCode,
                focus,
                input.multStepsLbl,
                input.productCode,
                input.serialId,
            )
        )

        return Results(
            tickets
        )
    }

    data class Params(
        val siteCode: Int,
        val isFocus: Boolean,
        val multStepsLbl: String?,
        val productCode: Int? = null,
        val serialId: String? = null,
    )

    data class Results(
        val tickets: List<HMAux>,
    )

    companion object{
        class Factory(val context: Context): NamoaFactory<GetTicketActionUseCase>() {
            override fun build(): GetTicketActionUseCase {
                return GetTicketActionUseCase(
                    TicketRepositoryImp(
                        context,
                        TK_TicketDao(context)
                    )
                )
            }
        }
    }
}
