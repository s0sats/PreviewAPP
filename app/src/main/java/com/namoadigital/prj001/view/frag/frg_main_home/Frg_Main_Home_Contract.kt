package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.DialogInterface
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.MdTag
import com.namoadigital.prj001.model.MenuMainNamoa
import com.namoadigital.prj001.service.WS_TK_Ticket_Save
import java.util.*

interface Frg_Main_Home_Contract {

    interface View{
        fun showMsg(ttl: String, msg: String)
        //
        fun showAlert(ttl: String, msg: String, listenerOk: DialogInterface.OnClickListener?, showNegative: Boolean)
        //
        fun setWsProcess(wsProcess: String)
        //
        fun showPD(ttl: String, msg: String)
        //
        fun loadMenuV3(tags: MdTag)
    }

    interface Presenter{
        fun getMenuItensV3()

        fun applyFilter(dataRangeFilter: Int, siteFIlter: Int)

        fun showFilterDialog()
        
        fun processWS_SaveReturn(wsRet: String?)
    }

}