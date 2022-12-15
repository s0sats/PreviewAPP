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

                    val actionList = mutableListOf<MyActionsBase>()

                    actionList.addAll(
                        repository.getLocalOpenTickets(localTicket, mainUser).map {
                            TK_Ticket.toMyActionsObj(context, it, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getTicketCache(localTicket, mainUser).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )

                    actionList.addAll(
                        repository.getSchedules(localTicket, mainUser).map {
                            it.toMyActionsObj(context, getLastSelectedPk())
                        }
                    )
                    //
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
                    //

                    val processActionList = mutableListOf<MyActionsBase>()
                    processActionList.addAll(actionList)
                    if(serialModel.userFocus == 0) {
                        val processRemoteActionList = mutableListOf<MyActionsBase>()
                        processRemoteActionList.addAll(
                            repository.getUnfocusAndHistorical(
                                serialModel.productCode ?: -1,
                                (serialModel.serialCode ?: -1).toLong(),
                                serialModel.serialId ?: "",
                                "OPEN"
                            )
                        )
                        if(processRemoteActionList.size >0) {
                            processRemoteActionList.forEach {
                                var found = false
                                var myActions = it as MyActions
                                for (action in actionList as List<MyActions>) {
                                    if (myActions.processId == action.processId) {
                                        found = true
                                        break
                                    }
                                }
                                if(!found){
                                    processActionList.add(myActions)
                                }
                            }
                        }
                    }
                    //
                    processActionList.sortBy {
                        when (it) {
                            is MyActions -> it.orderBy
                            else -> "190001010000"
                        }
                    }
                    //
                    val historicalList = mutableListOf<MyActionsBase>()
                    val historicalRemoteList = mutableListOf<MyActionsBase>()
                    val historicalLocalList = mutableListOf<MyActionsBase>()

                    if(serialModel.userFocus == 0) {
                        historicalRemoteList.addAll(
                            repository.getUnfocusAndHistorical(
                                serialModel.productCode ?: -1,
                                (serialModel.serialCode ?: -1).toLong(),
                                serialModel.serialId ?: "",
                                "HIST"
                            )
                        )
                        //
                        historicalLocalList.addAll(
                            repository.getUnfocusSchedules(localTicket).map {
                                it.toMyActionsObj(context, getLastSelectedPk())
                            }
                        )
                        //
                        historicalLocalList.addAll(
                            repository.getLocalHistoricalTickets(localTicket).map {
                                TK_Ticket.toMyActionsObj(context, it, getLastSelectedPk())
                            }
                        )
                        historicalList.addAll(historicalLocalList)
                        if (historicalRemoteList.size > 0) {

                            historicalRemoteList.forEach {
                                var found = false
                                var action = it as MyActions
                                //
                                for (unfocusAction in historicalLocalList) {
                                    if (action.processId == (unfocusAction as MyActions).processId) {
//                                        action = action.mergeUnfocusActions(unfocusAction)
                                        found = true
                                        break
                                    }
                                }
                                //
                                if(!found){
                                    historicalList.add(action)
                                }
                            }
                            //
                            historicalList.sortByDescending {
                                when (it) {
                                    is MyActions -> it.orderBy
                                    else -> "190001010000"
                                }
                            }
                            //
                            actionBaseList.addAll(processActionList)
                            actionBaseList.addAll(historicalList)
                        }
                    } else {
                        actionBaseList.addAll(processActionList)
                    }
                    //
                    val actions = if (mainUser)
                        actionBaseList.map { m -> m as MyActions }
                            .filter { f -> f.isMainUserTicket } as MutableList<MyActionsBase>
                    else actionBaseList
                    //
                    emit(loading(false))
                    emit(success(actions.toMutableList()))
                    //
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
