package com.namoadigital.prj001.extensions.serial

import android.content.Context
import com.namoadigital.prj001.extensions.date.convertDateToFullTimeStampGMT
import com.namoadigital.prj001.extensions.formatForDisplay
import com.namoadigital.prj001.extensions.roundByRestrictionMeasure
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.MDProductSerialVg
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
