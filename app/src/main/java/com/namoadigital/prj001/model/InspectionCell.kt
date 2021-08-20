package com.namoadigital.prj001.model

import androidx.annotation.ColorInt
import java.io.Serializable

data class InspectionCell(
    val description: String,
    val status: String,
    @ColorInt val tagColor: Int,
    val dayCount: Int,
    val photoCount: Int?,
    val productCount: Int?,
    val hasComment: Boolean?,
    val verificationActionLbl: String?,
    val autoSkipLbl: String?,
    val answer: String?,
    var isDone: Boolean = false
): Serializable {

    init{
        answer?.let {
            isDone = true
        }
    }

    fun getAllFieldForFilter() : String{
        return  "$description|" +
                "$answer|" +
                "$status|"
                    .replace("null|","")
                    .replace("null","")
    }
}