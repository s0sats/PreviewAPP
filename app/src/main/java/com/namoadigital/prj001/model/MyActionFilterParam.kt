package com.namoadigital.prj001.model

import java.io.Serializable

class MyActionFilterParam(
        var tagFilter: Int? = null,
        var productCode: Int? = null,
        var serialId: String? = null,
        var clientId: String? = null,
        var contractId: String? = null,
        var ticketId: String? = null,
        var calendarDate: String? = null
) : Serializable{
    companion object{
        const val  MY_ACTION_FILTER_PARAM = "MY_ACTION_FILTER_PARAM"
    }
}