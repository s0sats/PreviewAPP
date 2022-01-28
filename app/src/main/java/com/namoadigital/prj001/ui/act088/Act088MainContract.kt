package com.namoadigital.prj001.ui.act088

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux

interface Act088MainContract {

    interface I_View{
        fun callAct001()
        fun callAct034(bundle: Bundle, sendBroadcast: Boolean = false)
    }

    interface I_Presenter{
        fun getTranslation(): HMAux
        fun defineFlow(customerCode: Long, hmActivityStatus: HMAux?)
    }
}