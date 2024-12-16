package com.namoadigital.prj001.ui.act087.model

import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop
import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop.NO_STOPPED

data class InitialSerialState(
    val desiredDate: String?,
    val stoppedDate: String?,
    val responsibleStop: ResponsibleStop = NO_STOPPED,
    val showResponsableStopMachine: Boolean=false,
    val isTicketSerialStopped:Boolean = false,
    val isEditMode: Boolean,
    val horimeter:Double?,
    val horimeter_date:String?,
    val horimeter_supplier_uid:String?,
    val horimeter_supplier_desc:String?,
    val measure_block_input_time:Long?,
    val measure_alert_input_time:Long?,
)
