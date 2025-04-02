package com.namoadigital.prj001.ui.act070.model

import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.model.BaseSerialSearchItem

data class ListSerialSearchItemsArguments(
    val list: List<BaseSerialSearchItem>,
    val translateMap: TranslateMap
) {
    object ARGUMENTS {
        const val LIST = "list"
        const val TRANSLATE_MAP = "translate_map"
    }
}

