package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isFailed
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FlowScheduleFromMyActionUseCase constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
    private val processFormUseCase: ProcessFormUseCase,
    private val processTicketUseCase: ProcessTicketUseCase,
    private val getScheduleFromMyActionUseCase: GetScheduleFromMyActionUseCase
) : UseCases<MyActions, FlowScheduleFromMyActionUseCase.FlowScheduleParamReturn> {


    data class FlowScheduleParamReturn(
        val actType: String = Constant.ACT005,
        var scheduleExec: MD_Schedule_Exec,
        var action: MyActions,
        val ticketPrefix: Int = 0,
        val ticketCode: Int = 0,
        val ticketSeq: Int = 1,
    )

    override suspend fun invoke(input: MyActions): Flow<IResult<FlowScheduleParamReturn>> {
        return flow {
            val scheduleExec = getScheduleFromMyActionUseCase(input)
            scheduleExec?.let {
                when (it.schedule_type) {
                    ConstantBaseApp.MD_SCHEDULE_TYPE_FORM -> {
                        processFormUseCase(ProcessFormUseCase.ProcessFormParam(input, it)).collect {
                            it.isSuccess { retorno ->
                                emit(success(retorno))
                            }

                            it.isFailed { exception ->
                                emit(failed(exception))
                            }
                        }
                    }

                    else -> {
                        if (ToolBox_Inf.profileExists(
                                context,
                                ConstantBaseApp.PROFILE_MENU_TICKET,
                                null
                            )
                        ) {
                            processTicketUseCase(
                                ProcessTicketUseCase.ProcessTicketParam(
                                    input, it
                                )
                            ).collect {
                                it.isSuccess { retorno ->
                                    emit(success(retorno))
                                }

                                it.isFailed { throwable ->
                                    emit(failed(throwable))
                                }
                            }
                        } else {
                            emit(failed(ScheduleFormException(PROFILE_MENU_TICKET_NOT_FOUND)))
                        }
                    }
                }
            }

        }


    }

    companion object {
        const val PROFILE_MENU_TICKET_NOT_FOUND = "PROFILE_MENU_TICKET_NOT_FOUND"


        fun isScheduleSiteDifferentThanLogged(siteCode: Int?, context: Context): Boolean {
            siteCode?.let {
                return "$it" != ToolBox_Con.getPreference_Site_Code(context)
            } ?: return false
        }

        suspend fun hasScheduleSiteAccess(
            siteCode: String,
            repository: ActionSerialRepository
        ): Boolean {
            var access = false

            val formSite = repository.getSite(siteCode)

            formSite?.let {
                if (it.site_code.equals(siteCode)) {
                    access = true
                }
            }

            return access
        }
    }
}

class ScheduleFormException(override val message: String) : IOException(message)