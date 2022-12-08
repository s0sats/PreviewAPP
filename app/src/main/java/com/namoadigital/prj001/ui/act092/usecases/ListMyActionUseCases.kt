package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.ui.act092.model.SerialModel
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
) : UseCases<Pair<SerialModel, Boolean>, MutableList<MyActionsBase>> {
    private val actionBaseList = mutableListOf<MyActionsBase>()

    override suspend fun invoke(input: Pair<SerialModel, Boolean>): Flow<IResult<MutableList<MyActionsBase>>> {
        actionBaseList.clear()
        return flow {
            val serialModel = input.first
            val mainUser = input.second
            with(serialModel) {
                copy(
                    customerCode = ToolBox_Con.getPreference_Customer_Code(context).toInt(),
                    siteCode = if (setSiteFilter(context)) ToolBox_Con.getPreference_Translate_Code(
                        context
                    ) else null,
                    multStepsLbl = hmAux?.get("other_steps_available_lbl")
                ).let { localTicket ->

                    emit(loading(true))

                    val focusList = mutableListOf<MyActionsBase>()

                    focusList.addAll(
                        repository.getLocalTickets(localTicket, mainUser).map {
                            TK_Ticket.toMyActionsObj(context, it, getLastSelectedPk())
                        }
                    )

                    focusList.addAll(
                        repository.getTicketCache(localTicket, mainUser).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    focusList.addAll(
                        repository.getSchedules(localTicket, mainUser).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

/* A Confirmar
                    actionBaseList.addAll(
                        repository.getFormAp(localTicket).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )*/

                    focusList.addAll(
                        repository.getLocalForms(localTicket).map {
                            GE_Custom_Form_Local.toMyActionsObj(
                                context,
                                it,
                                getLastSelectedPk(),
                                false
                            )
                        }
                    )
                    //
                    focusList.sortBy {
                        when (it) {
                            is MyActions -> it.orderBy
                            else -> "190001010000"
                        }
                    }
                    //
                    val unfocusList = mutableListOf<MyActionsBase>()
                    unfocusList.addAll(
                        repository.getUnfocusAndHistorical(
                            serialModel.productCode ?: -1,
                            (serialModel.serialCode ?: -1).toLong(),
                            serialModel.serialId ?: ""
                        )
                    )

                    //
                    if (unfocusList.size > 0 && serialModel.userFocus == 1) {
                        val unfocusTemp = mutableListOf<MyActions>()

                        val focusTemp = focusList.map {
                            var action = it as MyActions
                            for (unfocusAction in unfocusList) {
                                if (action.processId == (unfocusAction as MyActions).processId) {
                                    action.mergeUnfocusActions(unfocusAction)
                                    unfocusTemp.add(unfocusAction)
                                }
                            }
                            it as MyActionsBase
                        }
                        //
                        unfocusList.sortByDescending {
                            when (it) {
                                is MyActions -> it.orderBy
                                else -> "190001010000"
                            }
                        }
                        //
                        val filteredUnfocusList = unfocusList.filter {
                            var insertItem = true
                            for (unfocusTempAction in unfocusTemp) {
                                if ((it as MyActions).processId == (unfocusTempAction as MyActions).processId) {
                                    insertItem = false
                                }
                            }
                            //
                            insertItem
                        }
                        //
                        actionBaseList.addAll(focusTemp)
                        actionBaseList.addAll(filteredUnfocusList)
                    } else {
                        actionBaseList.addAll(focusList)
                    }

                    val actions = if (mainUser)
                        actionBaseList.map { m -> m as MyActions }
                            .filter { f -> f.isMainUserTicket } as MutableList<MyActionsBase>
                    else actionBaseList

                    emit(loading(false))
                    emit(success(actions.toMutableList()))

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
        ) && !ToolBox_Inf.hasSoOrIOProfile(context)
}
