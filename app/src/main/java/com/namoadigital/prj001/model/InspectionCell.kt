package com.namoadigital.prj001.model

import androidx.annotation.ColorInt
import com.namoadigital.prj001.R
import java.io.Serializable

data class InspectionCell(
    val description: String,
    val dayCount: Int,
    val photoCount: Int= 0,
    val photoRequired: Boolean,
    val materialCount: Int = 0,
    val materialRequired: Boolean,
    val hasComment: Boolean = false,
    val commentRequired: Boolean,
    var status: String,
    val autoSkipLbl: String?,
    val isCritical: Boolean,
    val isNewItem: Boolean = false,
    val answer: String?,
    val itemPk: String
): Serializable {
    var isDone: Boolean = false
    @ColorInt
     var tagColor: Int

    init{
        answer?.let {
            isDone = true
            if(photoRequired && photoCount == 0){
                isDone = false
            }
            if(materialRequired && materialCount == 0){
                isDone = false
            }
            if(commentRequired && !hasComment){
                isDone = false
            }
        }
        //
        if(isDone){
            this.status = ANSWERED
            tagColor = R.color.namoa_os_form_verified_green
        }else{
            this.status = status
            when(status){
                NORMAL -> {
                    tagColor = R.color.namoa_color_gray_6
                }
                MANUAL_ALERT -> {
                    tagColor = R.color.namoa_os_form_problem_red
                }
                else -> {
                    tagColor = R.color.namoa_color_pipeline_origin_icon
                    if(isCritical){
                        tagColor = R.color.namoa_os_form_critical_forecast_yellow
                    }
                }
            }
        }
    }

    fun getAllFieldForFilter() : String{
        return  "$description|" +
                "$answer|" +
                "$status|"
                    .replace("null|","")
                    .replace("null","")
    }
    companion object{
        const val ANSWERED = "answered"
        const val NORMAL = "normal"
        const val MANUAL_ALERT = "manual_alert"
        const val FORECAST = "forecast"
        const val CRITICAL_FORECAST = "critical_forecast"
    }
}

enum class InspectionCellActions{
    VERIFY , VERIFY_LATER
}