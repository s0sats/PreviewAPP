package com.namoadigital.prj001.ui.act047.model

import com.namoa_digital.namoa_library.ctls.SearchableSpinner
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.SO_Next_Orders_Obj
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

data class NextOsFilter(
    var statusFilter: List<TypeStatusFilter>,
    var deadlineFilter: List<TypeDeadlineFilter>,
    var priorityFilter: TypePriorityFilter,
    var priorityTypeFilter: String = "",
) {
    fun statusListFilterToString() = statusFilter.map { it.type }

    fun deadlineFilterToString() = deadlineFilter.map { it.type }

    fun statusFilterToService(): List<String> {
        return statusFilter.flatMap { filter ->
            when (filter) {
                TypeStatusFilter.PENDING_AND_PROCESS -> listOf(
                    ConstantBaseApp.SYS_STATUS_PENDING,
                    ConstantBaseApp.SYS_STATUS_PROCESS
                )

                else -> listOf(filter.type)
            }
        }
    }

    fun filterList(
        list: ArrayList<SO_Next_Orders_Obj>,
        priorityTypeFilter: String = ""
    ): ArrayList<SO_Next_Orders_Obj> {
        val listFilter = list.filter { item ->
            statusFilter.any { status ->
                if (status == TypeStatusFilter.PENDING_AND_PROCESS) {
                    item.status == ConstantBaseApp.SYS_STATUS_PENDING || item.status == ConstantBaseApp.SYS_STATUS_PROCESS
                } else {
                    item.status == status.type
                }
            } && (deadlineFilter.isEmpty() || deadlineFilter.any { deadline ->
                when (deadline) {
                    TypeDeadlineFilter.NOT_EXPIRED -> !ToolBox_Inf.isItemLate(item.deadline) && !item.deadline.isNullOrEmpty()
                    TypeDeadlineFilter.EXPIRED -> ToolBox_Inf.isItemLate(item.deadline)
                    TypeDeadlineFilter.WITHOUT -> item.deadline.isNullOrEmpty()
                }
            }) && (priorityTypeFilter.isEmpty() || item.priority_desc == priorityTypeFilter)
        }

        val filterSort = when (priorityFilter) {
            TypePriorityFilter.DEADLINE -> {
                listFilter.sortedWith(compareByDescending<SO_Next_Orders_Obj> { it.priority_weight }.thenByDescending {
                    ToolBox_Inf.isItemLate(
                        it.deadline
                    )
                })
            }

            TypePriorityFilter.DATE_CREATED -> {
                listFilter.sortedWith(compareByDescending<SO_Next_Orders_Obj> { it.priority_weight }.thenBy {
                    ToolBox_Inf.dateToMilliseconds(
                        it.createDate,
                        ""
                    )
                })
            }
        }



        return ArrayList(filterSort)
    }

    fun getPartnerList(list: ArrayList<HMAux>) =
        ArrayList(list.map { it[SearchableSpinner.DESCRIPTION] }).toSet().toList()


}


enum class TypePriorityFilter(val type: String) {
    DEADLINE("PRIORITY_DEADLINE"),
    DATE_CREATED("PRIORITY_DATE_CREATED")
}

enum class TypeDeadlineFilter(val type: String) {
    NOT_EXPIRED("NOT_EXPIRED"),
    EXPIRED("EXPIRED"),
    WITHOUT("WITHOUT_DEADLINE")
}

enum class TypeStatusFilter(val type: String) {

    EDIT(Constant.SYS_STATUS_EDIT),
    BUDGET(Constant.SYS_STATUS_WAITING_BUDGET),
    APPROVAL_QUALITY(Constant.SYS_STATUS_WAITING_QUALITY),
    PENDING_AND_PROCESS(Constant.SYS_STATUS_PENDING + "_" + Constant.SYS_STATUS_PROCESS),
    STOP(Constant.SYS_STATUS_STOP),
    APPROVAL_FINAL(Constant.SYS_STATUS_WAITING_CLIENT),
}

