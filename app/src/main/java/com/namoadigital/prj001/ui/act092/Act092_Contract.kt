package com.namoadigital.prj001.ui.act092

import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView
import kotlinx.coroutines.flow.MutableStateFlow

interface Act092_Contract {

    interface View : BaseView<Act092UiEvent> {
        var wsProcess: MutableStateFlow<String>
    }

    interface Presenter : BasePresenter<View> {

        fun getMyActionList(userFocus: Boolean = false)
        fun onBackPressedClicked(bundle: Bundle)
        fun syncFiles(context: Context)
    }
}