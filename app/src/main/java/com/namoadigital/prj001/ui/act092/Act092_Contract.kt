package com.namoadigital.prj001.ui.act092

import android.os.Bundle
import com.namoadigital.prj001.ui.act092.utils.Act092UiEvent
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView

interface Act092_Contract {

    interface View : BaseView<Act092UiEvent> {
        fun callAct006(bundle: Bundle)
        fun callAct016(bundle: Bundle)
        fun callAct068()
        fun callAct005()
    }

    interface Presenter : BasePresenter<View> {

        fun getMyActionList()
        fun onBackPressedClicked()

    }
}