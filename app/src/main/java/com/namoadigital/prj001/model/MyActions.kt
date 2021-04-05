package com.namoadigital.prj001.model

import android.content.res.AssetFileDescriptor

data class MyActions(
        val processId: String,
        val processStatus: String,
        val processLeftIcon: Int?,
        val processRightIcon: Int,
        val plannedDate: String,
        val productDesc: String,
        val serialId: String,
        val originDescriptor: String,
        val processDesc: String,
        val focusStepDesc: String,
        val siteDesc: String,
        val clientInfo: String,
        val contractInfo: String,
        val serviceOrderCode: String,
        val doneDate: String
)
