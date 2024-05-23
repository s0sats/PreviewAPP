package com.namoadigital.prj001.core.trip.domain.usecase

import android.content.Context
import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepository
import com.namoadigital.prj001.core.data.local.repository.ticket.TicketCacheRepositoryImp
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.ui.base.NamoaFactory

class GetTicketCacheActionUseCase constructor(
    private val repository: TicketCacheRepository,
): UseCaseWithoutFlow<GetTicketCacheActionUseCase.Params, GetTicketCacheActionUseCase.Results> {

    override fun invoke(input: Params): Results {
        val tickets = mutableListOf<TkTicketCache>()
        var otherTickets: Int= 0
        val focus = if(input.isFocus) 1 else 0
        val unfocus = if(!input.isFocus) 1 else 0

        tickets.addAll(repository.getTicketActionList(input.siteCode, focus))

        otherTickets += repository.getTicketActionList(
            input.siteCode,
            unfocus,
        ).size

        return Results(
            tickets
        )
    }

    data class Params(
        val siteCode: Int,
        val isFocus: Boolean
    )

    data class Results(
        val tickets: List<TkTicketCache>
    )

    companion object{
        class Factory(val context: Context): NamoaFactory<GetTicketCacheActionUseCase>() {
            override fun build(): GetTicketCacheActionUseCase {
                return GetTicketCacheActionUseCase(
                    TicketCacheRepositoryImp(
                        context,
                        TkTicketCacheDao(context)
                    )
                )
            }
        }
    }
}
