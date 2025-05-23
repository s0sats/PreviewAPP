package com.namoadigital.prj001.extensions

import android.util.Log
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.roundByRestrictionMeasure(restrictionMeasure: Int?):Double{
    return BigDecimal(this).setScale(restrictionMeasure?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT, RoundingMode.HALF_UP).toDouble()
}

fun roundOffDecimal(number: Double, pattern:String = "#.###"): Double {
    val dfs = DecimalFormatSymbols()
    dfs.decimalSeparator = '.'
    val df = DecimalFormat(pattern, dfs)
    return try {
        df.roundingMode = RoundingMode.CEILING
        df.format(number).toDouble()
    }catch (e:Exception){
        ToolBox_Inf.registerException("roundOffDecimal", e)
        0.0
    }
}

fun Double?.toStringConsiderDecimal():String?{
    return this?.let{
        if (this % 1.0 == 0.0) {
            this.toInt().toString()
        } else {
            this.toString()
        }
    }
}