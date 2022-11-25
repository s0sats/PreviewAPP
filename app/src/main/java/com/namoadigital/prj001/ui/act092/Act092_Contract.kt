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
        var wsProcess: MutableStateFlow<String>
        val focusState: StateFlow<FilterFocusUser>
        val filterText: MutableStateFlow<String>
    }

    interface Presenter : BasePresenter<View> {
        val serialModel: StateFlow<SerialModel>
        fun getMyActionList(mainFocus: Boolean = false)
        fun onBackPressedClicked(bundle: Bundle)
        fun syncFiles(context: Context)
        fun getUnfocusHistoricalList()
        fun syncFilesForm(productCode: Long = 1L)

        //actions type
        fun processActionClick(action: MyActions, context: Context)
        fun processNewFormClick(context: Context)
        fun verifyProductOutdateForForm(hmAux: HMAux, context: Context): Boolean
        fun getCacheTicketBundle(hmAuxTicketDownloaded: HMAux): Bundle
        fun processWsReturnSync(hmAuxTicketDownload: HMAux)
    }
}