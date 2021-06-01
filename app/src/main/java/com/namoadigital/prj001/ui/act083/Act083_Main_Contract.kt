package com.namoadigital.prj001.ui.act083

import android.os.Bundle
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsFormButton

interface Act083_Main_Contract {
    interface I_View {
        fun showMsg(type: String, item: MyActions)
        fun showAlertMsg(ttl: String, msg: String)
        fun showToast(msg: String)
        fun callAct070(bundle: Bundle)
        fun callAct071(bundle: Bundle)
        fun callAct033()
        fun setProcess(wsProcess: String)
        fun showPD(ttl: String?, msg: String?)
        fun callAct038(formApBundle: Bundle)
        fun callAct011(formBundle: Bundle)
        fun addControlToActivity(mketSerial: MKEditTextNM)
        fun removeControlFromActivity(mketSerial: MKEditTextNM)
        fun callAct020(bundle: Bundle)
        fun changeProgressBarVisility(show: Boolean)
        fun iniRecycler()
        fun callAct009(bundle: Bundle)
        fun callAct005()
        fun callAct006(bundle: Bundle)
        fun callAct016(bundle: Bundle)
        fun callAct068(bundle: Bundle)
        fun getCurrentTab(): Int
        fun getMketFilter(): String?
        fun setViewFiltersParam(textFilter: String?, initialTabToLoad: Int)
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
        fun prepareWsTicketDownload(myAction: MyActions)
        fun isScheduleStarted(myAction: MyActions): Boolean
        fun isScheduleFormType(myAction: MyActions): Boolean
        fun hasScheduleSiteAccess(siteCode: Int?): Boolean
        fun verifyProductOutdateForForm(hmAuxTicketDownloaded: HMAux): Boolean
        fun prepareWsFormSync()
        fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle
        fun processActionClick(myAction: MyActions)
        fun checkFormFlow(action: MyActions)
        fun extractSearchResult(result: String?)
        fun processActionFormButtonClick(myActionsFormButton: MyActionsFormButton)
        fun onBackPressedClicked()
    }

}