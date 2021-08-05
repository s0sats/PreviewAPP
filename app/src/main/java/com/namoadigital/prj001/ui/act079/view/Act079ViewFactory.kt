package com.namoadigital.prj001.ui.act079.view

import android.content.Context
import com.namoadigital.prj001.model.TkTicketOriginNc

class Act079ViewFactory(
    private val context: Context,
    private val ticketOriginNc: TkTicketOriginNc
){
    fun get() : Act079ViewNcBase{
        return when(ticketOriginNc.getCustomFormDataType()){
            TkTicketOriginNc.TAB -> Act079ViewNcTab(context,ticketOriginNc)
            else -> Act079ViewNcField(context,ticketOriginNc)
        }
    }

}
