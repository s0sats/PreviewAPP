package com.namoadigital.prj001.ui.act091

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.SOExpressItemHeader
import com.namoadigital.prj001.model.TSO_Service_Search_Obj

sealed interface Act091_Contract {


    interface I_View {
        fun callAct040(expressTmp: Long)
        fun openBottomSheet(itemHeader: SOExpressItemHeader)
    }

    interface I_Presenter {
        fun getTranslation(): HMAux?
        fun getListData() : List<TSO_Service_Search_Obj>
        fun savePackServices(contentItemHeader: SOExpressItemHeader)
    }


}