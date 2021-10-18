package com.namoadigital.prj001.model

import androidx.annotation.ColorInt
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.ConstantBaseApp
import java.io.Serializable

data class InspectionCell   (
    val description: String,
    val dayCount: Int,
    val photoCount: Int= 0,
    val materialCount: Int = 0,
    val materialRequired: Boolean,
    val hasComment: Boolean = false,
    val commentRequired: Boolean,
    var status: String,
    val isCritical: Boolean,
    val isNewItem: Boolean = false,
    val answerStatus: String?,
    val execType: String?,
    val itemCodeAndSeq: String,
    val hmAuxTrans: HMAux
): Serializable {
    var isDone: Boolean = false
    @ColorInt
    var tagColor: Int
    var statusTransalted: String = ""
    var execTypeTranslated: String = ""
    init{
        answerStatus?.let {
            isDone = !it.equals(ConstantBaseApp.SYS_STATUS_PROCESS)
        }
        //
        if(isDone){
            tagColor = R.color.namoa_color_light_green5
            statusTransalted = hmAuxTrans["inpection_status_answered_item_lbl"]!!
        }else{
            this.status = status
            when(status){
                NORMAL -> {
                    tagColor = R.color.namoa_color_gray_6
                    statusTransalted = hmAuxTrans["inpection_status_non_forecast_item_lbl"]!!
                }
                MANUAL_ALERT -> {
                    tagColor = R.color.namoa_os_form_problem_red
                    statusTransalted = hmAuxTrans["inpection_status_manual_alert_item_lbl"]!!
                }
                else -> {
                    if(isCritical){
                        this.status = CRITICAL_FORECAST
                        tagColor = R.color.namoa_os_form_critical_forecast_yellow
                        statusTransalted = hmAuxTrans["inpection_status_critical_forecast_item_lbl"]!!
                    }else{
                        this.status = FORECAST
                        tagColor = R.color.namoa_color_pipeline_origin_icon
                        statusTransalted = hmAuxTrans["inpection_status_forecast_item_lbl"]!!
                    }
                }
            }
        }

        when(execType){
            GeOsDeviceItem.EXEC_TYPE_FIXED -> {
                execTypeTranslated = hmAuxTrans["inpection_answer_fixed_lbl"]!!
            }
            GeOsDeviceItem.EXEC_TYPE_ALERT ->{
                execTypeTranslated = hmAuxTrans["inpection_answer_alert_lbl"]!!
            }
            GeOsDeviceItem.EXEC_TYPE_ALREADY_OK -> {
                execTypeTranslated = hmAuxTrans["inpection_answer_already_ok_lbl"]!!
            }
            GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED -> {
                execTypeTranslated = hmAuxTrans["inpection_answer_not_verify_lbl"]!!
            }
        }
    }

    fun getAllFieldForFilter() : String{
        return  "$description|" +
                "$execTypeTranslated|" +
                "$statusTransalted|"
                    .replace("null|","")
    }
    companion object{
        const val ANSWERED = "ANSWERED"
        const val NORMAL = "NORMAL"
        const val MANUAL_ALERT = "MANUAL_ALERT"
        const val FORECAST = "FORECAST"
        const val CRITICAL_FORECAST = "CRITICAL_FORECAST"
    }
}

