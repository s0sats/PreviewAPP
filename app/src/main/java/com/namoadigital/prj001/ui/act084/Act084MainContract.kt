package com.namoadigital.prj001.ui.act084

import android.os.Bundle
import com.namoadigital.prj001.model.MyActions

interface Act084MainContract {
    interface I_View {
        fun iniRecycler()
        fun changeProgressBarVisility(show: Boolean)
        fun callAct011(bundle: Bundle)
        fun getMketFilter(): String?
        fun getCurrentTab(): Int
        fun callAct070(bundle: Bundle)
        fun callAct038(bundle: Bundle)
        fun showMsg(ttl: String?, msg: String?)
        fun setViewFiltersParam(mketFilter: String?, tabToLoad: Int,ncFilter: Boolean)
        fun getNcFilterStatus() : Boolean
    }

    interface I_Presenter {
        fun updateMyActionList(userFocusFilter: Int, ncFilterOn: Boolean)
        fun processActionClick(myAction: MyActions)
    }
}