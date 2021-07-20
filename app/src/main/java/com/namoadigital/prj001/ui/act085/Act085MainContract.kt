package com.namoadigital.prj001.ui.act085

import com.namoa_digital.namoa_library.util.HMAux

interface Act085MainContract {

    interface I_View{
        fun setWsProcess(wsProcess: String)
        fun showPD(ttl: String , msg: String)
    }

    interface I_Presenter{
        fun getTranslation(): HMAux
        fun executeWorkgroupEditService(action: Int, vararg workgroupCode: Int)
        fun executeWorkgroupMemberListService(user_code: Int)
    }
}