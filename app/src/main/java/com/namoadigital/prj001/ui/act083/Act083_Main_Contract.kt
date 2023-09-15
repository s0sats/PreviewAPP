package com.namoadigital.prj001.ui.act083

import android.os.Bundle
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
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
        fun iniRecycler(list: MutableList<MyActionsBase>)
        fun callAct009(bundle: Bundle)
        fun callAct005()
        fun callAct006(bundle: Bundle)
        fun callAct016(bundle: Bundle)
        fun callAct068(bundle: Bundle)
        fun getCurrentTab(): Int
        fun getMketFilter(): String?
        fun getMainUserFilter(): Boolean
        fun setViewFiltersParam(
            textFilter: String?,
            initialTabToLoad: Int,
            mainUserFilterState: Boolean
        )
        fun setTabsCounters(selectedTabCounter: Int, otherTabCounter: Int)
        fun updateFooterInfos()
        fun setPlaceholderTextAndVisibility(currentTabCounter: Int)
        fun resetActionPosition()
        fun callAct092(bundle: Bundle)
        fun changeTitleTopBar(siteDesc: String)
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
        fun prepareWsFormSync(productCode: Long = 0)
        fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle
        fun processActionClick(myAction: MyActions)
        fun checkFormFlow(action: MyActions)
        fun extractSearchResult(result: String?, myActionSelected: MyActions?)
        fun processActionFormButtonClick(myActionsFormButton: MyActionsFormButton)
        fun onBackPressedClicked()
        fun processWsSyncReturn(hmAuxTicketDownload: HMAux)
        fun getMainUserFiltersParam(): Boolean
        fun processSerialClick(
            serialId: String,
            productCode: Int?,
            productId: String = "",
            myAction: MyActions? = null
        )

        fun processLocalSearchForSerialAction(
            selectedActionForSerialFLow: MyActions,
            mdProductSerial: MD_Product_Serial?
        )

        fun extractStructureResult(serial: MD_Product_Serial, myAction: MyActions?)
        fun updateSharedPrefs()

        fun getJustifyItems(justifyGroupCode: Int): ArrayList<HMAux>

        fun justifyNotExecuteSchedule(
            processPk: String,
            comments: String,
            justify_group_code: Int,
            justify_item_code: Int,
            reschedule_date: String
        )

        fun processSerialSite(tabUserFocusFilter: Int)
        fun getSerialSiteInventoryList(tabUserFocusFilter: Int)
        fun checkSerialSiteInv()
    }

}