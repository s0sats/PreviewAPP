package com.namoadigital.prj001.model

import androidx.annotation.DrawableRes

data class MyActions(
        val processId: String,
        val processStatus: String,
        @DrawableRes val  processLeftIcon: Int?,
        @DrawableRes val processRightIcon: Int?,
        val plannedDate: String,
        val tagOperationDesc: String,
        val productDesc: String,
        val serialId: String,
        val originDescriptor: String,
        val processDesc: String,
        val focusStepDesc: String?,
        val siteDesc: String?,
        val clientInfo: String?,
        val contractInfo: String?,
        val serviceOrderCode: String?,
        val doneDate: String?,
        val orderBy: String
)
