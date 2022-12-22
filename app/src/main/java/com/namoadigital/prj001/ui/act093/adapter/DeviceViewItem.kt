package com.namoadigital.prj001.ui.act093.adapter

import com.namoadigital.prj001.ui.act093.model.DeviceTpModel

sealed class DeviceViewItem {

    data class SectionItem(val color: Int) : DeviceViewItem()
    data class ContentItem(val item: DeviceTpModel, val color: Int) :
        DeviceViewItem()

    companion object {
        const val VIEW_TYPE_SECTION = 1
        const val VIEW_TYPE_ITEM = 2
    }

}