package com.namoadigital.prj001.ui.act089

import android.content.Context

interface Act089MainContract {
    interface I_View{
        fun setWsProcess(wsProcessSupport: String)
        fun showPD(ttl: String, msg: String)
    }

    interface I_Presenter{
        fun rebuildDatabase()
        fun sendSupport(support_contact: String, support_msg: String)
        fun getDbError() : String
    }
}