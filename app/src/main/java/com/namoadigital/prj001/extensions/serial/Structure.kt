package com.namoadigital.prj001.extensions.serial

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.dao.md.MDProductSerialVGDao
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.formatForDisplay
import com.namoadigital.prj001.extensions.roundByRestrictionMeasure
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.extensions.watchStatus
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MD_Product_Serial_Structure
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg
import com.namoadigital.prj001.sql.transaction.DatabaseTransactionManager
import com.namoadigital.prj001.util.ToolBox_Inf


fun MDProductSerialVg.formatLastValue(context: Context, restrictionDecimal: Int?, valueSufix: String?): String? {
    val lastExecutionMeasure = this.lastExecutionMeasure?.roundByRestrictionMeasure(restrictionDecimal)?.toStringConsiderDecimal()?.let {
        "$it ${valueSufix.formatForDisplay()}"
    }

    val dateFormatted = this.lastExecutionDate?.let{
        ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(it.convertDateToFullTimeStampGMT("dd/MM/yyyy HH:mm:ss Z")),
            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
        )?: ""
    }
    if(dateFormatted != null) {
        return """$dateFormatted ${lastExecutionMeasure?.let { "(${it})" } ?: ""}"""
    }
    return null
}

fun GeOsVg.formatLastValue(context: Context, restrictionDecimal: Int?, valueSufix: String?): String? {
    val lastExecutionMeasure = this.lastExecutionMeasure?.roundByRestrictionMeasure(restrictionDecimal)?.toStringConsiderDecimal()?.let {
        "$it ${valueSufix.formatForDisplay()}"
    }
    val dateFormatted = this.lastExecutionDate?.let{
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(it.convertDateToFullTimeStampGMT("dd/MM/yyyy HH:mm:ss Z")),
                ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
            )
        }
    if(dateFormatted != null) {
        return """$dateFormatted ${lastExecutionMeasure?.let { "(${it})" } ?: ""}"""
    }
    return null
}

fun MD_Product_Serial.refreshStructureHeader(serialStructure: MD_Product_Serial_Structure){
        //
        this.has_item_check  = serialStructure.has_item_check
        this.scn_item_check  = serialStructure.scn_item_check
        this.measure_tp_code  = serialStructure.measure_tp_code
        this.last_measure_value  = serialStructure.last_measure_value
        this.last_measure_date  = serialStructure.last_measure_date
        this.last_cycle_value  = serialStructure.last_cycle_value
        this.last_cycle_date  = serialStructure.last_cycle_date
        this.log_date  = ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")
        this.horimeter  = serialStructure.horimeter
        this.horimeter_date  = serialStructure.horimeter_date
        this.horimeter_supplier_uid  = serialStructure.horimeter_supplier_uid
        this.horimeter_supplier_desc  = serialStructure.horimeter_supplier_desc
        this.measure_block_input_time  = serialStructure.measure_block_input_time
        this.measure_alert_input_time  = serialStructure.measure_alert_input_time
        this.unavailability_reason_option  = serialStructure.unavailability_reason_option
        this.syncStructure = 0
        this.syncBigFile = 0
        //
}

fun MD_Product_Serial.executeDbTransaction(
    context: Context,
    serialDao: MD_Product_SerialDao,
    tpDeviceDao: MD_Product_Serial_Tp_DeviceDao,
    productSerialVGDao: MDProductSerialVGDao,
    serialStructure: MD_Product_Serial_Structure
): Boolean{
        var result = false
        DatabaseTransactionManager(context).executeTransactionDaoObjReturn { database ->
            serialDao.addUpdate(this)
            var daoObjReturn = serialDao.removeFullStructure(this)

            if (!daoObjReturn.hasError()) {
                daoObjReturn = tpDeviceDao.addUpdate(
                    serialStructure.device_tp,
                    false,
                    database
                )
            }

            if (!daoObjReturn.hasError()) {
                daoObjReturn = productSerialVGDao.addUpdate(
                    serialStructure.verificationGroup,
                    false,
                    database
                )
            }

            daoObjReturn
        }.watchStatus(
            success = {
                result = true
            },
            failed = {
                result = false
            }
        )
        return result

}
fun MD_Product_Serial.setStructuresPK(serialStructure: MD_Product_Serial_Structure){
    serialStructure.device_tp.forEach {
        it.setPk(this)
    }

    serialStructure.verificationGroup?.forEach {
        it.updatePk(this)
    }
}

