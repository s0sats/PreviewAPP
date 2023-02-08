package com.namoadigital.prj001.adapter.searchableitem

import com.namoa_digital.namoa_library.util.HMAux

data class ItemSearchableModel(
    val code: String? = null,
    val text: String? = null
) {
    companion object {
        fun List<HMAux>.convertToItemSearchable(
            code: String,
            desc: String
        ): List<ItemSearchableModel> {
            val newList = mutableListOf<ItemSearchableModel>()
            this.map { list ->
                if (list.containsKey(code) && list.containsKey(desc)) {
                    newList.add(ItemSearchableModel(list[code], list[desc]))
                }
            }
            return newList
        }

    }

}
