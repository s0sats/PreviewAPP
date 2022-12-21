package com.namoadigital.prj001.ui.act093

import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView
import kotlinx.coroutines.flow.StateFlow

interface Contract {

    interface View : BaseView<Act093Event> {
    }

    interface Presenter : BasePresenter<View> {
        val state: StateFlow<Act093State>
    }

}