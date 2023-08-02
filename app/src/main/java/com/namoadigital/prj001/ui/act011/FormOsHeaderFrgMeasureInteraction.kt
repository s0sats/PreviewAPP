package com.namoadigital.prj001.ui.act011

import com.namoadigital.prj001.model.MeMeasureTp

interface FormOsHeaderFrgMeasureInteraction {
    fun getMeasure(customerCode: Long, measureCode: Int) : MeMeasureTp?
}