package com.namoadigital.prj001.extensions.serial

import android.content.Context
import android.view.View
import android.widget.TextView
import com.namoadigital.prj001.util.ToolBox_Inf

fun TextView.showMeasureSuffixAndDate(
    context: Context,
    value: String?,
    suffix: String?,
    date: String?
) {
    this.visibility = date?.let{
        val formattedDate = ToolBox_Inf.millisecondsToString(
            ToolBox_Inf.dateToMilliseconds(date),
            ToolBox_Inf.nlsDateFormat(context)
        )
        val formattedValue = value?.let{
            "($it${suffix?.let{" $it"}?:""})"
        }?: ""

        this.text = "$formattedDate $formattedValue"
        View.VISIBLE
    }?:View.GONE

}
