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
                    //
                    val ticketList : List<MyActionsBase> = setTicketList(localTicket, mainUser, serialModel)
                    val scheduleList : List<MyActionsBase>  = setScheduleList(localTicket, mainUser, serialModel)
                    val formList : List<MyActionsBase>  = setFormList(localTicket, mainUser, serialModel)
                    //
                    val processList = mutableListOf<MyActionsBase>()
                    processList.addAll(ticketList)
                    processList.addAll(scheduleList)
                    processList.addAll(formList)
                    //
                    val filterOpen: MutableList<MyActionsBase> = processList.filter {
                        val myActions = it as MyActions
                        myActions.doneDate == null
                    } as MutableList<MyActionsBase>
                    //
                    filterOpen.sortBy {
                        when (it) {
                            is MyActions -> it.orderBy
                            else -> "190001010000"
                        }
                    }
                    //
                    actionBaseList.addAll(filterOpen)
                    //
                    if (serialModel.userFocus == 0) {
                        val filterDone: MutableList<MyActionsBase> = processList.filter {
                            val myActions = it as MyActions
                            myActions.doneDate != null
                        } as MutableList<MyActionsBase>

                        filterDone.sortByDescending {
                                when (it) {
                                    is MyActions -> it.orderBy
                                    else -> "190001010000"
                                }
                            }
                        actionBaseList.addAll(filterDone)
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

    private suspend fun setFormList(
        localTicket: SerialModel,
        mainUser: Boolean,
        serialModel: SerialModel
    ): List<MyActionsBase> {
        val localList = mutableListOf<MyActionsBase>()
        //
        localList.addAll(
            repository.getLocalForms(localTicket).map {
                GE_Custom_Form_Local.toMyActionsObj(
                    context,
                    it,
                    serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_FORM),
                    false
                )
            }
        )
        val processFormList = mutableListOf<MyActionsBase>()

        if (serialModel.userFocus == 0) {
            val remoteList = mutableListOf<MyActionsBase>()
            remoteList.addAll(
                repository.getUnfocusAndHistorical(
                    serialModel.productCode ?: -1,
                    (serialModel.serialCode ?: -1).toLong(),
                    serialModel.serialId ?: "",
                    null
                ).filter {
                    it.actionType == MyActions.MY_ACTION_TYPE_FORM
                }
            )
            processFormList.addAll(remoteList)
            //
            localList.forEach {
                var found = false
                var myActions = it as MyActions
                for (action in remoteList as List<MyActions>) {
                    if (myActions.processId == action.processId) {
                        found = true
                        break
                    }
                }
                if(!found){
                    processFormList.add(myActions)
                }
            }

        }else{
            processFormList.addAll(localList)
        }
        return processFormList
    }

    private suspend fun setScheduleList(
        localTicket: SerialModel,
        mainUser: Boolean,
        serialModel: SerialModel
    ): List<MyActionsBase> {
        val localList = mutableListOf<MyActionsBase>()
        localList.addAll(
            repository.getSchedules(localTicket, mainUser).map {
                it.toMyActionsObj(context, serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_SCHEDULE))
            }
        )
        val processScheduleList = mutableListOf<MyActionsBase>()

        if (serialModel.userFocus == 0) {
            //
            localList.addAll(
                repository.getUnfocusSchedules(localTicket).map {
                    it.toMyActionsObj(context, serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_SCHEDULE))
                }
            )
            //
            val remoteList = mutableListOf<MyActionsBase>()
            remoteList.addAll(
                repository.getUnfocusAndHistorical(
                    serialModel.productCode ?: -1,
                    (serialModel.serialCode ?: -1).toLong(),
                    serialModel.serialId ?: "",
                    null
                ).filter {
                    it.actionType == MyActions.MY_ACTION_TYPE_SCHEDULE
                }
            )
            processScheduleList.addAll(remoteList)
            //
            localList.forEach {
                var found = false
                var myActions = it as MyActions
                for (action in remoteList as List<MyActions>) {
                    if (myActions.processId == action.processId) {
                        found = true
                        break
                    }
                }
                if(!found){
                    processScheduleList.add(myActions)
                }
            }
        }else{
            processScheduleList.addAll(localList)
        }
        return processScheduleList
    }

    private suspend fun setTicketList(
        localTicket: SerialModel,
        mainUser: Boolean,
        serialModel: SerialModel
    ): List<MyActionsBase> {
        val localList = mutableListOf<MyActionsBase>()
        localList.addAll(
            repository.getLocalOpenTickets(localTicket, mainUser).map {
                TK_Ticket.toMyActionsObj(context, it, serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET))
            }
        )
        //
        localList.addAll(
            repository.getTicketCache(localTicket, mainUser).map {
                it.toMyActionsObj(context, serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET))
            }
        )
        //
        val processTicketList = mutableListOf<MyActionsBase>()

        if (serialModel.userFocus == 0) {
            val remoteList = mutableListOf<MyActionsBase>()
            //
            localList.addAll(
                repository.getLocalHistoricalTickets(localTicket).map {
                    TK_Ticket.toMyActionsObj(context, it, serialModel.getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET))
                }
            )
            processTicketList.addAll(localList)
            //
            remoteList.addAll(
                repository.getUnfocusAndHistorical(
                    serialModel.productCode ?: -1,
                    (serialModel.serialCode ?: -1).toLong(),
                    serialModel.serialId ?: "",
                    null
                ).filter {
                    it.actionType == MyActions.MY_ACTION_TYPE_TICKET
                            || it.actionType == MyActions.MY_ACTION_TYPE_TICKET_CACHE
                }
            )
            //
            remoteList.forEach {
                var found = false
                var myActions = it as MyActions
                for (action in localList as List<MyActions>) {
                    if (myActions.processId == action.processId) {
                        found = true
                        break
                    }
                }
                if(!found){
                    processTicketList.add(myActions)
                }
            }

        }else{
            processTicketList.addAll(localList)
        }
        return processTicketList
    }

    private fun setSiteFilter(context: Context): Boolean =
        ConstantBaseApp.PREFERENCE_HOME_CURRENT_SITE_OPTION == ToolBox_Con.getStringPreferencesByKey(
            context,
            ConstantBaseApp.PREFERENCE_HOME_SITES_FILTER,
            ConstantBaseApp.PREFERENCE_HOME_ALL_SITE_OPTION
        ) && !ToolBox_Inf.hasSoOrIOProfile(context)
}
