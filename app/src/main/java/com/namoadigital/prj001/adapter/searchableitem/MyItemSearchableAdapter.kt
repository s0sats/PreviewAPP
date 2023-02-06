package com.namoadigital.prj001.adapter.searchableitem

import com.namoa_digital.namoa_library.util.HMAux

data class MyItemSearchableAdapter(
    val code: String? = null,
    val text: String? = null
) {
    fun getAllFieldForFilter(showPrice: Boolean): String? {
        return text
    }

    companion object {
        fun List<HMAux>.convertToMyItemSearchable(
            code: String,
            desc: String
        ): List<MyItemSearchableAdapter> {
            val newList = mutableListOf<MyItemSearchableAdapter>()
            this.map { list ->
                if (list.containsKey(code) && list.containsKey(desc)) {
                    newList.add(MyItemSearchableAdapter(list[code], list[desc]))
                }
            }
            return newList
        }

    }

}
