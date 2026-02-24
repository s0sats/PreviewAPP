package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.error
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.event.EventManualDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.data.repository.IActionSerialRepository
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.FlowTicketAccessError.Companion.EVENT_NOT_ACCESS
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.FlowTicketAccessError.Companion.SITE_ACCESS_CONFIRM
import com.namoadigital.prj001.ui.act092.usecases.FlowTicketAccessUseCase.FlowTicketAccessError.Companion.SITE_NOT_ACCESS
import com.namoadigital.prj001.ui.act095.event_manual.data.EventManualRepositoryImpl
import com.namoadigital.prj001.ui.act095.event_manual.domain.usecases.GetEventManualUseCase
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlowTicketAccessUseCase(
    private val context: Context,
    private val repository: ActionSerialRepository,
    private val getEventManualUseCase: GetEventManualUseCase
) : UseCases<String, Unit> {

    //input = ticket site_code
    override suspend fun invoke(input: String): Flow<IResult<Unit>> {
        return flow {
            val hasAccessSite = repository.getSite(input) != null
            val eventManual = getEventManualUseCase(Unit)

            eventManual?.eventSiteCode?.takeIf {
                it != input.toInt()
            }?.let {
                emit(error(EVENT_NOT_ACCESS))
                return@flow
            }

            if (!ToolBox_Inf.equalsToLoggedSite(context, input)) {
                if (!hasAccessSite) {
                    emit(error(SITE_NOT_ACCESS))
                } else {
                    emit(error(SITE_ACCESS_CONFIRM))
                }
            } else {
                emit(success(Unit))
            }
        }
    }

    class FlowTicketAccessError {
        companion object {
            const val EVENT_NOT_ACCESS = "EVENT_NOT_ACCESS"
            const val SITE_NOT_ACCESS = "SITE_NOT_ACCESS"
            const val SITE_ACCESS_CONFIRM = "SITE_ACCESS_CONFIRM"
        }
    }

    companion object {
        class Factory(private val context: Context) : NamoaFactory<FlowTicketAccessUseCase>() {
            override fun build(): FlowTicketAccessUseCase {
                val repository = IActionSerialRepository.Companion.ActionSerialRepositoryFactoryRepository(
                    context
                ).build()

                return FlowTicketAccessUseCase(context, repository, GetEventManualUseCase(
                    repository = EventManualRepositoryImpl(
                        appContext = context,
                        typeDao = FSEventTypeDao(context),
                        eventDao = EventManualDao(context),
                        fileDao = GE_FileDao(context)
                    )))
            }
        }
    }
}