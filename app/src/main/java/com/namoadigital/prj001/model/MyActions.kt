package com.namoadigital.prj001.model

import androidx.annotation.DrawableRes

data class MyActions(
        val actionType: String,
        val processPk: String,
        val processId: String?,
        val processStatus: String,
        @DrawableRes val processLeftIcon: Int?,
        @DrawableRes val processRightIcon: Int?,
        val plannedDate: String,
        val tagOperationDesc: String,
        val productDesc: String,
        val serialId: String,
        val originDescriptor: String,
        val processDesc: String,
        val focusStepDesc: String?,
        val siteDesc: String?,
        val clientInfo: String?,
        val contractInfo: String?,
        val serviceOrderCode: String?,
        val doneDate: String?,
        val orderBy: String,
        val ticketOriginType: String?,
        val highlightItem: Boolean,
        val periodStarted: Boolean,
        val lateItem: Boolean
){
    companion object{
        const val MY_ACTION_TYPE_TICKET = "TICKET"
        const val MY_ACTION_TYPE_TICKET_CACHE = "TICKET_CACHE"
        const val MY_ACTION_TYPE_SCHEDULE = "SCHEDULE"
        const val MY_ACTION_TYPE_FORM_AP = "FORM_AP"
        const val MY_ACTION_TYPE_FORM = "FORM"
    }
    var siteCode: Int? = null
    var productCode: Int? = null
    var customFormTypeDesc: String? = null
    var customFormDesc: String? = null

    fun getSplippedPk(): List<String> {
        return processPk.split(".")
    }

    fun getAllFieldForFilter() : String{
        return  "$processId|" +
                "$processStatus|" +
                "$plannedDate|" +
                "$tagOperationDesc|" +
                "$productDesc|" +
                "$serialId|" +
                "$originDescriptor|" +
                "$processDesc|" +
                "$focusStepDesc|" +
                "$siteDesc|" +
                "$clientInfo|" +
                "$contractInfo|" +
                "$serviceOrderCode"
                .replace("null|","")
                .replace("null","")
    }
}
