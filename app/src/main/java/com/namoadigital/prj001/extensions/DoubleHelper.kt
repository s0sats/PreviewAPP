package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.util.ConstantBaseApp
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundByRestrictionMeasure(restrictionMeasure: Int?):Double{
    return BigDecimal(this).setScale(restrictionMeasure?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT, RoundingMode.HALF_UP).toDouble()
}