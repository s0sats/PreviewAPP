package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act092.core.IResult
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.failed
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.loading
import com.namoadigital.prj001.ui.act092.core.IResult.Companion.success
import com.namoadigital.prj001.ui.act092.core.UseCases
import com.namoadigital.prj001.ui.act092.core.extension.namoaCatch
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel
import com.namoadigital.prj001.ui.act092.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ListMyActionUseCases constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
) : UseCases<LocalTicketsModel, MutableList<MyActionsBase>> {
    val actionList = mutableListOf<MyActionsBase>()

    override suspend fun invoke(input: LocalTicketsModel): Flow<IResult<MutableList<MyActionsBase>>> {
        return flow {
            with(input) {
                copy(
                    customerCode = ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                    siteCode = if (setSiteFilter(context)) ToolBox_Con.getPreference_Translate_Code(
                        context
                    ) else null,
                    multStepsLbl = hmAux?.get("other_steps_available_lbl")
                ).let { localTicket ->

                    emit(IResult.loading(true))

                    actionList.addAll(
                        repository.getLocalTickets(localTicket).map {
                            TK_Ticket.toMyActionsObj(context, it, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getTicketCache(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getSchedules(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getFormAp(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getLocalForms(localTicket).map {
                            GE_Custom_Form_Local.toMyActionsObj(
                                context,
                                it,
                                getLastSelectedPk(),
                                false
                            )
                        }
                    )

                    actionList.sortBy {
                        when (it) {
                            is MyActions -> it.orderBy
                            is MyActionsFormButton -> it.orderBy
                            else -> "190001010000"
                        }
                    }

                    emit(loading(false))
                    emit(success(actionList))

                }
            }

        }.namoaCatch(ListMyActionUseCases::class.toString()).catch { e ->
            emit(loading(false))
            emit(failed(e))
            actionList.sortBy {
                when (it) {
                    is MyActions -> it.orderBy
                    is MyActionsFormButton -> it.orderBy
                    else -> "190001010000"
                }
            }
            delay(4000)
            emit(success(actionList))
        }
    }

    private fun setSiteFilter(context: Context): Boolean =
        ConstantBaseApp.PREFERENCE_HOME_CURRENT_SITE_OPTION == ToolBox_Con.getStringPreferencesByKey(
            context,
            ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER,
            ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION
        )
                && !ToolBox_Inf.hasSoOrIOProfile(context)
}
