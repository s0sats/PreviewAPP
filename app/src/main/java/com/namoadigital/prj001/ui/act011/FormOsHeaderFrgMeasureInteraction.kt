package com.namoadigital.prj001.ui.act011

import com.namoadigital.prj001.model.MeMeasureTp
import com.namoadigital.prj001.model.TK_Ticket_Form

interface FormOsHeaderFrgMeasureInteraction {
    fun getMeasure(customerCode: Long, measureCode: Int) : MeMeasureTp?
    fun getTkTicketFormContinuous(): TK_Ticket_Form?
}