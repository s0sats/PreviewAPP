package com.namoadigital.prj001.ui.act091

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.SOExpressItemHeader
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Obj

sealed interface Act091_Contract {


    interface I_View {
        fun callAct040(expressTmp: Long)
        fun openBottomSheet(item: TSO_Service_Search_Obj)
    }

    interface I_Presenter {
        fun getTranslation(): HMAux?
        fun getListData() : List<TSO_Service_Search_Obj>
        fun savePackServices(contentItemHeader: SoPackExpressPacksLocal)
        fun getSO_Pack_Express_Local(): SO_Pack_Express_Local
    }


}