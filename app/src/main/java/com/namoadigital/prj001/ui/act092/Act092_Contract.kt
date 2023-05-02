package com.namoadigital.prj001.ui.act092

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.act092.utils.FilterFocusUser
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Act092_Contract {

    interface View : BaseView<Act092UiEvent> {

        val CHANGE_ZONE_RESULT_CODE: Int
            get() = 10
        var wsProcess: MutableStateFlow<String>
        val focusState: StateFlow<FilterFocusUser>
        val filterText: MutableStateFlow<String>
        var bundle: Bundle
        fun showPD(ttl: String?, msg: String?)
        fun disablePD()
        fun setItemAsDownloaded(position: Int, myActions: MyActions)
    }

    interface Presenter : BasePresenter<View> {
        fun getActionSelected(): MyActions?
        val serialModel: StateFlow<SerialModel>
        fun getMyActionList()
        fun onBackPressedClicked(bundle: Bundle)
        fun syncFiles(context: Context)
        fun getUnfocusHistoricalList(context: Context)
        fun syncFilesForm(productCode: Long = 1L)
        //actions type
        fun processActionClick(action: MyActions, context: Context, position: Int)
        fun processNewFormClick(context: Context)
        fun verifyProductOutdateForForm(hmAux: HMAux, context: Context): Boolean
        fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle
        fun processWsReturnSync(hmAuxTicketDownload: HMAux)
        fun checkScheduleFlow(action: MyActions)
        fun callFormSave(context: Context)
        fun callTicketSave(context: Context)
        fun otherActionFlow(context: Context)
        fun extractSearchResult(result: String?, myActionSelected: MyActions?)
        fun executeNFormPDFGeneration(context: Context, action: MyActions, position: Int)
        fun goToInfoSerial()
        fun saveFilterWhenLeftActivity()
        fun justifyNotExecuteSchedule(
            processPk: String,
            comments: String,
            justify_group_code: Int,
            justify_item_code: Int,
            reschedule_date: String,
            context: Context,
        )

        fun getJustifyItems(justifyGroupCode: Int, context: Context): ArrayList<HMAux>
    }
}