package com.namoadigital.prj001.ui.act095.event_manual.presentation.historic.domain

import com.namoadigital.prj001.model.MyActionsBase

data class EventHistoricToMyActionsBase(
    val date: String,
    val eventDay: Int,
    val quantity: Int,
) : MyActionsBase()