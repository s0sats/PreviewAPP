package com.namoadigital.prj001.ui.act091

import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.model.TSO_Service_Search_Obj

sealed interface Act91_Contract {


    interface I_View {
        fun callAct040()
        fun openBottomSheet(item: Act091ServiceItem)
    }

    interface I_Presenter {
        fun getListData() : List<TSO_Service_Search_Obj>
    }


}