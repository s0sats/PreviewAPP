package com.namoadigital.prj001.ui.act091.mvp.presenter

import com.namoadigital.prj001.model.TSO_Service_Search_Obj

sealed class Act091Event {

    data class OpenBottomSheet(val item: TSO_Service_Search_Obj) : Act091Event()

}