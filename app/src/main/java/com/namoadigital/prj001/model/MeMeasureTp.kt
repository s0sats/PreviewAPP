package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import com.namoa_digital.namoa_library.ctls.MeasureFF
import com.namoa_digital.namoa_library.util.ConstantBase.DATEFORMATDB
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.extensions.roundByRestrictionMeasure
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
    @SerializedName("restriction_min") val restrictionMin: Double?,
    @SerializedName("restriction_max") val restrictionMax: Double?,
    @SerializedName("restriction_decimal") val restrictionDecimal: Int? = 4,
    @SerializedName("value_cycle_size") val valueCycleSize: Float?,
    @SerializedName("cycle_tolerance") val cycleTolerance: Int?,
    @SerializedName("without_measure") val without_measure: Int = 0
) {

    fun isMeasureRestrictionInvalid(
        bypassMinValidation: Boolean,
        measureValue: Float,
        lastMeasureValue: Float?,
        lastMeasureDate: String?,
        measureDate: String?
    ): Boolean{
        return validateMeasureRestriction(
            bypassMinValidation,
            measureValue,
            lastMeasureValue,
            lastMeasureDate,
            measureDate
        ).isValid.not()
    }


    fun validateMeasureRestriction(
        bypassMinValidation: Boolean,
        measureValue: Float,
        lastMeasureValue: Float?,
        lastMeasureDate: String?,
        measureDate: String?
    ): MeasureFF.MeasureValidationReturn {
        val consideredLastMeasureValue = lastMeasureValue?.roundByRestrictionMeasure(restrictionDecimal?:ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT)
        if(bypassMinValidation
            || without_measure == 1){
            return MeasureFF.MeasureValidationReturn(true)
        }
         return when (this.restrictionType) {
            RESTRICTION_TYPE_VALUE -> isMeasureRestrictionValueValid(
                measureValue,
                consideredLastMeasureValue
            )
            RESTRICTION_TYPE_VALUE_BY_DAY -> isMeasureRestrictionValueByDayValid(
                measureValue,
                consideredLastMeasureValue,
                lastMeasureDate,
                measureDate
            )
            RESTRICTION_TYPE_MIN_MAX -> isMeasureRestrictionMinMaxValid(measureValue)
            else -> MeasureFF.MeasureValidationReturn(true)
        }
    }

    private fun isMeasureRestrictionValueValid(
        typedMeasure: Float,
        lastMeasureValue: Float?
    ): MeasureFF.MeasureValidationReturn {
        lastMeasureValue?.let { lastMeasure ->
            val minConsider: Double? = if (restrictionMin != null) {
                lastMeasure.minus(restrictionMin)
            } else {
                null
            }
            val maxConsider: Double? = if (restrictionMax != null) {
                lastMeasure.plus(restrictionMax)
            } else {
                null
            }
            //
            return validateValues(minConsider, typedMeasure, maxConsider)
            //
//            if (minConsider != null && maxConsider != null) {
//                return minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(
//                    typedMeasure
//                ) >= 0
//            } else if (minConsider != null || maxConsider != null) {
//                return if (minConsider != null) {
//                    minConsider.compareTo(typedMeasure) <= 0
//                } else {
//                    maxConsider!!.compareTo(typedMeasure) >= 0
//                }
//            }
        }
        //
        return MeasureFF.MeasureValidationReturn(true, null)
    }

    private fun isMeasureRestrictionValueByDayValid(
        typedMeasure: Float,
        lastMeasureValue: Float?,
        lastMeasureDate: String?,
        measureDate: String?
    ): MeasureFF.MeasureValidationReturn {
        if (lastMeasureValue != null && lastMeasureDate != null) {
            //Como considera a data e inicio para calculo, se ela for invalida, o value by day tb será, pois não há como calcular.
            measureDate?.let {
                if (!isValidStartDate(lastMeasureDate, measureDate)) {
                    return MeasureFF.MeasureValidationReturn(false, null)
                }
                val valPerDay = getDiffBetweenDatesInFloatDays(lastMeasureDate!!, measureDate!!)
                //Se o valor for menor do que 0, considerar 0
                val minConsider: Double? = restrictionMin?.let { min ->
                    val minToConsider = lastMeasureValue - (min * valPerDay)
                    if (minToConsider >= 0f) {
                        minToConsider
                    } else {
                        0
                    }
                } as Double?
                val maxConsider: Double? = restrictionMax?.let { max ->
                    lastMeasureValue + (max * valPerDay)
                }
                //
                return validateValues(minConsider, typedMeasure, maxConsider)
                //
//                if (minConsider != null && maxConsider != null) {
//                    if(minConsider.compareTo(typedMeasure) <= 0 && maxConsider.compareTo(
//                        typedMeasure
//                    ) >= 0){
//                        return MeasureFF.MeasureValidationReturn(false, null)
//                    }
//                    return MeasureFF.MeasureValidationReturn(false, null)
//                } else if (minConsider != null || maxConsider != null) {
//                    return if (minConsider != null) {
//                        minConsider.compareTo(typedMeasure) <= 0
//                    } else {
//                        maxConsider!!.compareTo(typedMeasure) >= 0
//                    }
//                }
            }?: return MeasureFF.MeasureValidationReturn(false, null)
        }
        return MeasureFF.MeasureValidationReturn(true, null)
    }

    private fun validateValues(
        minConsider: Double?,
        typedMeasure: Float,
        maxConsider: Double?
    ): MeasureFF.MeasureValidationReturn {

        minConsider?.let{
            val minConsiderRound = it.roundByRestrictionMeasure(restrictionDecimal?:ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT)
            if (typedMeasure.compareTo(minConsiderRound) < 0) {
                return MeasureFF.MeasureValidationReturn(false, UNDER_VALUE_ERROR)
            }
        }

        maxConsider?.let{
            val maxConsiderRound = it.roundByRestrictionMeasure(restrictionDecimal?:ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT)
            if (typedMeasure.compareTo(maxConsiderRound) > 0) {
                return MeasureFF.MeasureValidationReturn(false, OVER_VALUE_ERROR)
            }
        }

        return MeasureFF.MeasureValidationReturn(true, null)
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
    ): MeasureFF.MeasureValidationReturn {

        if (restrictionMin != null){
            if(restrictionMin > typedMeasure){
                return MeasureFF.MeasureValidationReturn(false, UNDER_VALUE_ERROR)
            }
        }

        if (restrictionMax != null){
            if(typedMeasure > restrictionMax){
                return MeasureFF.MeasureValidationReturn(false, OVER_VALUE_ERROR)
            }
        }

        return MeasureFF.MeasureValidationReturn(true, null)

//        if (restrictionMin != null && restrictionMax != null) {
//            if(restrictionMin <= typedMeasure){
//              if(typedMeasure <= restrictionMax){
//                  return MeasureFF.MeasureValidationReturn(false, OVER_VALUE_ERROR)
//              }
//                return MeasureFF.MeasureValidationReturn(true, null)
//            }
//
//        } else if (restrictionMin != null || restrictionMax != null) {
//            if (restrictionMin != null) {
//                if(restrictionMin <= typedMeasure){
//                    return MeasureFF.MeasureValidationReturn(true, null)
//                }
//                return MeasureFF.MeasureValidationReturn(false, UNDER_VALUE_ERROR)
//            } else {
//                if(typedMeasure <= restrictionMax!!){
//                    return MeasureFF.MeasureValidationReturn(true, null)
//                }
//                return MeasureFF.MeasureValidationReturn(false, OVER_VALUE_ERROR)
//
//            }
//        } else {
//            return MeasureFF.MeasureValidationReturn(true, null)
//        }
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

        const val OVER_VALUE_ERROR = "OVER_VALUE_ERROR"
        const val UNDER_VALUE_ERROR = "UNDER_VALUE_ERROR"
    }
}