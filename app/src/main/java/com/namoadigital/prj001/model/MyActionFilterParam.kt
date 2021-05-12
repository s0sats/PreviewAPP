package com.namoadigital.prj001.model

import java.io.Serializable

class MyActionFilterParam(
        var tagFilterCode: Int? = null,
        var tagFilterDesc: String? = null,
        var productCode: Int? = null,
        var productDesc: String? = null,
        var serialId: String? = null,
        var clientId: String? = null,
        var contractId: String? = null,
        var ticketId: String? = null,
        var calendarDate: String? = null
) : Serializable{
    companion object{
        const val  MY_ACTION_FILTER_PARAM = "MY_ACTION_FILTER_PARAM"
    }

    fun getFilledFilters() : List<String>{
        val filters = mutableListOf<String>()
        tagFilterDesc?.let {
            filters.add(it)
        }
        productDesc?.let {
            filters.add(it)
        }
        serialId?.let {
            filters.add(it)
        }
        clientId?.let {
            filters.add(it)
        }
        contractId?.let {
            filters.add(it)
        }
        ticketId?.let {
            filters.add(it)
        }
        calendarDate?.let {
            filters.add(it)
        }

        return filters
    }
}