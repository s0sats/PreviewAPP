package com.namoadigital.prj001.ui.act011.frags

import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.InspectionCell
import com.namoadigital.prj001.ui.act011.model.FormTicketInfo

interface InspectionListFragmentInteraction {
    fun onInspectionSelected(
        acessoryFormView: AcessoryFormView,
        isNewItem: Boolean,
        position: Int,
        searchFilterValue: String,
        chkStatus: Boolean,
        itemCodeAndSeqPk: String,
        partition_execution: Int = 0,
        ticketFormType: FormTicketInfo.TicketFormType = FormTicketInfo.TicketFormType.NO_TICKET
    )

    //
    fun onAlreadyOkAction(itemPk: String): InspectionCell
    fun onRefreshTabCounter(tabIndex: Int)
    fun getObjectView(position: Int): AcessoryFormView

    fun onSaveInitialMeasurement(itemPk: String, newMeasure: Double?, newID: String? = null)
}
