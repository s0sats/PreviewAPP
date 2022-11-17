package com.namoadigital.prj001.ui.act092.ui.adapter

import com.namoadigital.prj001.model.MyActionsBase

sealed class SerialViewItem {

    object SectionItem : SerialViewItem()
    data class ContentItem(val item: MyActionsBase) :
        SerialViewItem()

    companion object {
        const val VIEW_TYPE_SECTION = 1
        const val VIEW_TYPE_ITEM = 2
    }

}