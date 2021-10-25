package com.namoadigital.prj001.ui.act087

import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.MD_Product
import com.namoadigital.prj001.model.MdOrderType
import com.namoadigital.prj001.model.MeMeasureTp

interface FormOsHeaderFrgCreationInteraction {
    fun getOrderTypeList(orderTypeCode: Int = -1) : ArrayList<MdOrderType>
    fun searchSerialClick(bkpProductCode: Long,bkpSerialId: String)
    fun createOsHeader(formOsHeader: GeOs)
    fun getDefaultBkpMachineProduct() : MD_Product?
    fun getFormSerialId(): String
    fun delegateControlSta(control_sta: ArrayList<MKEditTextNM>, addAction: Boolean)
    fun getMeasure(measureCode: Int) : MeMeasureTp?
    fun getFormRequiresGPS(): Boolean
}