package com.namoadigital.prj001.model

import androidx.annotation.ColorInt
import com.namoadigital.prj001.R
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
    val answer: String?,
    val itemCodeAndSeq: String,
): Serializable {
    var isDone: Boolean = false
    @ColorInt
     var tagColor: Int

    init{
        answer?.let {
            isDone = true
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
                    if(isCritical){
                        this.status = CRITICAL_FORECAST
                        tagColor = R.color.namoa_os_form_critical_forecast_yellow
                    }else{
                        this.status = FORECAST
                        tagColor = R.color.namoa_color_pipeline_origin_icon
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
        const val ANSWERED = "ANSWERED"
        const val NORMAL = "NORMAL"
        const val MANUAL_ALERT = "MANUAL_ALERT"
        const val FORECAST = "FORECAST"
        const val CRITICAL_FORECAST = "CRITICAL_FORECAST"
    }
}

enum class InspectionCellActions(val action: String){
    VERIFY("verify") , VERIFY_LATER("verify_later"), ADD_NEW_ITEM("add_new_item")
}