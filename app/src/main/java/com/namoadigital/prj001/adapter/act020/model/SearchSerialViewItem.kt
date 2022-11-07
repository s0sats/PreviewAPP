package com.namoadigital.prj001.adapter.act020.model

import com.namoadigital.prj001.model.MD_Product_Serial

sealed class SearchSerialViewItem {

    data class SectionItem(val currentSite: Boolean) : SearchSerialViewItem()
    data class ContentItem(val product_serial: ProductSerialList, val product: MD_Product_Serial) :
        SearchSerialViewItem()

    companion object {
        const val VIEW_TYPE_SECTION = 1
        const val VIEW_TYPE_ITEM = 2
    }

}