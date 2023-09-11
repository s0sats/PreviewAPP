package com.namoadigital.prj001.ui.act093

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.util.Act093Event
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.ui.base.BasePresenter
import com.namoadigital.prj001.ui.base.BaseView
import kotlinx.coroutines.flow.StateFlow

interface Contract {

    interface View : BaseView<Act093Event> {
        fun onBack()
    }

    interface Presenter : BasePresenter<View> {
        val state: StateFlow<Act093State>
        fun getDeviceItemHist(context: Context, deviceItemRawPk: DeviceTpModel, hmAuxTrans: HMAux):  ArrayList<Act086HistoricModel>?
        fun getDeviceItem(context: Context, item: DeviceTpModel): MD_Product_Serial_Tp_Device_Item?
        fun getDeviceItemDaysInAlert(context: Context, item: DeviceTpModel): Long
    }

}