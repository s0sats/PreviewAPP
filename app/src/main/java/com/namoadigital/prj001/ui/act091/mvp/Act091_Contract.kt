package com.namoadigital.prj001.ui.act091.mvp

import com.namoadigital.prj001.databinding.Act091MainBinding
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.ui.act091.mvp.base.BasePresenter
import com.namoadigital.prj001.ui.act091.mvp.base.BaseView
import com.namoadigital.prj001.ui.act091.mvp.presenter.Act091Event
import com.namoadigital.prj001.ui.act091.mvp.ui.Act091EventUI

sealed interface Act091_Contract {

    interface I_View : BaseView<I_Presenter, Act091MainBinding, Act091EventUI> {

        fun callAct040(expressTmp: Long)
    }

    interface I_Presenter : BasePresenter<I_View, Act091Event> {
        fun getListData() : List<TSO_Service_Search_Obj>
        fun hasPermissionShowPrice(): Boolean
        fun savePackServices(contentItemHeader: SoPackExpressPacksLocal)
        fun getSO_Pack_Express_Local(): SO_Pack_Express_Local?
    }


}