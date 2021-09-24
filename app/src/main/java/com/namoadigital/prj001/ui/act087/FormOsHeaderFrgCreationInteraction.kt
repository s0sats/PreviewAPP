package com.namoadigital.prj001.ui.act087

import com.namoadigital.prj001.model.GeOs
import com.namoadigital.prj001.model.MD_Product
import com.namoadigital.prj001.model.MdOrderType

interface FormOsHeaderFrgCreationInteraction {
    fun getOrderTypeList(orderTypeCode: Int = -1) : ArrayList<MdOrderType>
    fun callProductSelection()
    fun searchSerialClick()
    fun createOsHeader(formOsHeader: GeOs)
}