package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act092.model.LocalTicketsModel
import com.namoadigital.prj001.ui.act092.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ListMyActionUseCases constructor(
    private val context: Context,
    private val repository: ActionSerialRepository,
) : UseCases<LocalTicketsModel, MutableList<MyActionsBase>> {
    val actionBaseList = mutableListOf<MyActionsBase>()

    override suspend fun invoke(input: LocalTicketsModel): Flow<IResult<MutableList<MyActionsBase>>> {
        actionBaseList.clear()
        return flow {
            with(input) {
                copy(
                    customerCode = ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                    siteCode = if (setSiteFilter(context)) ToolBox_Con.getPreference_Translate_Code(
                        context
                    ) else null,
                    multStepsLbl = hmAux?.get("other_steps_available_lbl")
                ).let { localTicket ->

                    emit(loading(true))

                    actionBaseList.addAll(
                        repository.getLocalTickets(localTicket).map {
                            TK_Ticket.toMyActionsObj(context, it, getLastSelectedPk())
                        }
                    )

                    actionBaseList.addAll(
                        repository.getTicketCache(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionBaseList.addAll(
                        repository.getSchedules(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionBaseList.addAll(
                        repository.getFormAp(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionBaseList.addAll(
                        repository.getLocalForms(localTicket).map {
                            GE_Custom_Form_Local.toMyActionsObj(
                                context,
                                it,
                                getLastSelectedPk(),
                                false
                            )
                        }
                    )

                    actionBaseList.sortBy {
                        when (it) {
                            is MyActions -> it.orderBy
                            is MyActionsFormButton -> it.orderBy
                            else -> "190001010000"
                        }
                    }

                    actionBaseList.addAll(
                        repository.getUnfocusAndHistorical(input.productCode?:-1, (input.serialCode?:-1).toLong())
                    )

                    val actions = actionBaseList.map { m -> m as MyActions }
                        .filter { f -> f.isMainUserTicket }

                    actionBaseList.addAll(
                        repository.getUnfocusAndHistorical(input.productCode?:-1, (input.serialCode?:-1).toLong())
                    )

                    val listOfficial = if (input.userFocus == 1) actions else actionBaseList

                    listOfficial?.let {

                    }
                    emit(loading(false))
                    emit(success(listOfficial.toMutableList()))

                }
            }

        }.catch { e ->
            ToolBox_Inf.registerException(
                ListMyActionUseCases::class.toString(),
                IOException(e.message)
            )
            emit(loading(false))
            emit(failed(e))
            actionBaseList.sortBy {
                when (it) {
                    is MyActions -> it.orderBy
                    is MyActionsFormButton -> it.orderBy
                    else -> "190001010000"
                }
            }
            delay(4000)
            emit(success(actionBaseList))
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
