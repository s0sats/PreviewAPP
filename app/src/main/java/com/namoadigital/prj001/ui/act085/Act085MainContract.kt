package com.namoadigital.prj001.ui.act085

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.TWorkgroupObj

interface Act085MainContract {
    interface I_View{
        fun setWsProcess(wsProcess: String)
        fun showPD(ttl: String , msg: String)
        fun showAlert(ttl: String, msg: String)
        fun updateWorkgroupMemberList(wgMemberList: List<TWorkgroupObj>)
        fun updateLinkeWorkgroupListIntoFrag(wgMemberList: List<TWorkgroupObj>)
        fun callAct005()
    }

    interface I_Presenter{
        fun getTranslation(): HMAux
        fun executeWorkgroupEditService(
            userCode: Int,
            action: Int,
            workgroupCode: ArrayList<Int>,
            limit: Int = 0,
            dateExpire: String? = null,
            expireReturn: Int? = null
        )
        fun executeWorkgroupMemberListService(userCode: Int)
        fun processWgMemberListReturn(mLink: String?)
        fun onBackPressedClick()
    }
}