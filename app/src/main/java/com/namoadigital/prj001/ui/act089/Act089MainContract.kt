package com.namoadigital.prj001.ui.act089

import android.content.Context

interface Act089MainContract {
    interface I_View{
        fun callLogout()
        fun setWsProcess(wsProcessSupport: String)
        fun showPD(ttl: String, msg: String)
    }

    interface I_Presenter{
        fun rebuildDatabase(context: Context)
        fun sendSupport(support_contact: String, support_msg: String)
    }
}