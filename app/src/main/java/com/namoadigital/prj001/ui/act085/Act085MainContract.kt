package com.namoadigital.prj001.ui.act085

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.TWorkgroupObj

interface Act085MainContract {
    interface I_View{
        fun setWsProcess(wsProcess: String)
        fun showPD(ttl: String , msg: String)
        fun showAlert(ttl: String, msg: String, positiveListener: DialogInterface.OnClickListener? = null, negativeOption: Int = 0)
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
            expireReturn: Int = 0
        )
        fun executeWorkgroupMemberListService(userCode: Int)
        fun processWgMemberListReturn(mLink: String?)
        fun getUnlinkedWgList(workgroupMemberList: List<TWorkgroupObj>): List<TWorkgroupObj>
        fun onBackPressedClick(fm: FragmentManager)
    }
}