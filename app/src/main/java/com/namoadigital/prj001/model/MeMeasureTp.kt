package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import com.namoa_digital.namoa_library.util.ConstantBase.DATEFORMATDB
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.math.BigDecimal
import java.math.RoundingMode

class MeMeasureTp(
    @SerializedName("customer_code") val customerCode: Long,
    @SerializedName("measure_tp_code") val measureTpCode: Int,
    @SerializedName("measure_tp_id") val measureTpId: String,
    @SerializedName("measure_tp_desc") val measureTpDesc: String,
    @SerializedName("value_sufix") val valueSufix: String?,
    @SerializedName("restriction_type") val restrictionType: String?,
    @SerializedName("restriction_min") val restrictionMin: Int?,
    @SerializedName("restriction_max") val restrictionMax: Int?,
    @SerializedName("restriction_decimal") val restrictionDecimal: Int? = 4,
    @SerializedName("value_cycle_size") val valueCycleSize: Float?,
    @SerializedName("cycle_tolerance") val cycleTolerance: Int?
) {

    fun isMeasureRestrictionInvalid(
        measureValue: Float,
        lastMeasureValue: Float?,
        lastMeasureDate: String?,
        measureDate: String?
    ): Boolean {
        return when (this.restrictionType) {
            RESTRICTION_TYPE_VALUE -> isMeasureRestrictionValueValid(
                measureValue,
                lastMeasureValue
            ).not()
            RESTRICTION_TYPE_VALUE_BY_DAY -> isMeasureRestrictionValueByDayValid(
                measureValue,
                lastMeasureValue,
                lastMeasureDate,
                measureDate
            ).not()
            RESTRICTION_TYPE_MIN_MAX -> isMeasureRestrictionMinMaxValid(measureValue).not()
            else -> false
        }
    }

    private fun isMeasureRestrictionValueValid(
        typedMeasure: Float,
        lastMeasureValue: Float?
    ): Boolean {
        lastMeasureValue?.let { lastMeasure ->
            val minConsider: Float? = if (restrictionMin != null) {
                lastMeasure.minus(restrictionMin)
            } else {
                null
            }
            val maxConsider: Float? = if (restrictionMax != null) {
                lastMeasure.plus(restrictionMax)
            } else {
                null
            }
            //
            if (minConsider != null && maxConsider != null) {
                return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(
                    typedMeasure
                ) >= 0
            } else if (minConsider != null || maxConsider != null) {
                return if (minConsider != null) {
                    minConsider.compareTo(typedMeasure) <= 0
                } else {
                    maxConsider!!.compareTo(typedMeasure) >= 0
                }
            }
        }
        //
        return true
    }

    private fun isMeasureRestrictionValueByDayValid(
        typedMeasure: Float,
        lastMeasureValue: Float?,
        lastMeasureDate: String?,
        measureDate: String?
    ): Boolean {
        if (lastMeasureValue != null && lastMeasureDate != null) {
            //Como considera a data e inicio para calculo, se ela for invalida, o value by day tb será, pois não há como calcular.
            measureDate?.let {
                if (!isValidStartDate(lastMeasureDate, measureDate)) {
                    return false
                }
                val valPerDay = getDiffBetweenDatesInFloatDays(lastMeasureDate!!, measureDate!!)
                //Se o valor for menor do que 0, considerar 0
                val minConsider: Float? = restrictionMin?.let { min ->
                    val minToConsider = lastMeasureValue!! - (min * valPerDay)
                    if (minToConsider >= 0f) {
                        minToConsider
                    } else {
                        0f
                    }
                }
                val maxConsider: Float? = restrictionMax?.let { max ->
                    lastMeasureValue!! + (max * valPerDay)
                }
                //
                if (minConsider != null && maxConsider != null) {
                    return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(
                        typedMeasure
                    ) >= 0
                } else if (minConsider != null || maxConsider != null) {
                    return if (minConsider != null) {
                        minConsider.compareTo(typedMeasure) <= 0
                    } else {
                        maxConsider!!.compareTo(typedMeasure) >= 0
                    }
                }
            }?: return false
        }
        return true
    }

    /**
     * Calcula a diferença de dias entre 2 datas como float
     */
    private fun getDiffBetweenDatesInFloatDays(lastMeasureDate: String, startDate: String): Float {
        //Data passada em MS
        val lastMeasureDateMs = ToolBox_Inf.dateToMilliseconds(lastMeasureDate)
        //Data atual em MS
        val startDateInMs = ToolBox_Inf.dateToMilliseconds(startDate)
        //Diferença entre das data em MS
        val diffInMs = startDateInMs - lastMeasureDateMs
        //Calc dias inteiros
        val calcDay = diffInMs / ConstantBaseApp.ONE_DAY_IN_MILLISECOND
        //Calc perc de dias...
        val modDay =
            (diffInMs % ConstantBaseApp.ONE_DAY_IN_MILLISECOND.toDouble()) / ConstantBaseApp.ONE_DAY_IN_MILLISECOND.toDouble()
        //Soma e devolve float com 4 casas.
        return BigDecimal(calcDay + modDay).setScale(4, RoundingMode.HALF_DOWN).toFloat()
    }

    private fun isMeasureRestrictionMinMaxValid(
        typedMeasure: Float
    ): Boolean {
        return if (restrictionMin != null && restrictionMax != null) {
            restrictionMin <= typedMeasure && typedMeasure <= restrictionMax
        } else if (restrictionMin != null || restrictionMax != null) {
            if (restrictionMin != null) {
                restrictionMin <= typedMeasure
            } else {
                typedMeasure <= restrictionMax!!
            }
        } else {
            true
        }
    }

    private fun isValidStartDate(lastMeasureDate: String, measureDate: String): Boolean {
        //
        var timeValues = measureDate.split(" ")
        //
        if (ToolBox.isValidDate(timeValues.get(0), DATEFORMATDB)
            && ToolBox_Inf.isValidHour24MinutesAndSecounds(timeValues.get(1) )
            && !ToolBox_Inf.isFutureDate(measureDate)
            && (ToolBox_Inf.dateToMilliseconds(lastMeasureDate) <= ToolBox_Inf.dateToMilliseconds(measureDate))
        ) {
            return true
        }
        return false
        //
    }

    companion object {
        const val RESTRICTION_TYPE_VALUE = "VALUE"
        const val RESTRICTION_TYPE_VALUE_BY_DAY = "VALUE_BY_DAY"
        const val RESTRICTION_TYPE_MIN_MAX = "MIN_MAX"
    }
}