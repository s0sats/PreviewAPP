package com.namoadigital.prj001.ui.act084

interface Act084MainContract {

    interface I_View {
        fun iniRecycler()
        fun changeProgressBarVisility(show: Boolean)
    }

    interface I_Presenter {
        fun updateMyActionList(userFocusFilter: Int, ncFilterOn: Boolean)

    }
}