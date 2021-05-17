package com.namoadigital.prj001.ui.act083

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.model.MyActions

interface Act083_Main_Contract {
    interface I_View {
        fun showMsg(type: String, item: MyActions)
        fun showAlertMsg(ttl: String, msg: String)
        fun showToast(msg: String)
        fun callAct070(bundle: Bundle)
        fun callAct071(bundle: Bundle)
        fun callAct033()
    }

    interface I_Presenter {
        fun loadTranslation(): HMAux?
        fun getChipList() : List<String>
        fun getActTitle(): String
        fun updateMyActionList(userFocusFilter: Int)
        fun checkScheduleFlow(myAction: MyActions)
        fun getLocalTicket(myAction: MyActions): Bundle
        fun getFormApBundle(myAction: MyActions): Bundle
        fun getFormBundle(myAction: MyActions): Bundle
        fun getLocalTickets(userFocus: Int): MutableList<HMAux>
        fun prepareWsTicketDownload(myAction: MyActions)
        fun isScheduleStarted(myAction: MyActions): Boolean
        fun isScheduleStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean
        fun isScheduleFormType(myAction: MyActions): Boolean
        fun isAnyFormInProcessing(myAction: MyActions): Boolean
        fun hasSerialDefined(myActions: MyActions): Boolean
        fun executeSerialSearch(productCode: Int?, productId: Int?, serialId: String, b: Boolean)
        fun getScheduleFormBundle(myAction: MyActions): Bundle
        fun getMdSchedule(myAction: MyActions): MD_Schedule_Exec
        fun hasScheduleSiteAccess(siteCode: Int?): Boolean
        fun verifyProductOutdateForForm(hmAuxTicketDownloaded: HMAux): Boolean
        fun prepareWsFormSync()
        fun getTicketFlowBundle(item: MyActions): Bundle
        fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle
        fun processTicketFlow(item: MyActions)

    }

}