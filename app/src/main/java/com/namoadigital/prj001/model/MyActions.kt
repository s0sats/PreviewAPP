package com.namoadigital.prj001.model

import androidx.annotation.DrawableRes

data class MyActions(
    val actionType: String,
    val processPk: String,
    val processId: String?,
    val processStatus: String,
    val processStatusTrans: String?,
    @DrawableRes var processLeftIcon: Int?,
    @DrawableRes var processMidIcon: Int?,
    @DrawableRes var processRightIcon: Int?,
    val plannedDate: String,
    val tagOperationDesc: String?,
    val productDesc: String,
    val serialId: String?,
    val originDescriptor: String,
    val processDesc: String,
    val internalComments: String?,
    val focusStepDesc: String?,
    val siteCode: Int? = null,
    val siteDesc: String?,
    val zoneDesc: String?,
    val serviceOrderCode: String?,
    val doneDate: String?,
    val orderBy: String,
    val ticketOriginType: String?,
    val scn: Int?,
    val highlightItem: Boolean,
    val periodStarted: Boolean,
    val lateItem: Boolean,
    var isLastSelectedItem: Boolean,
    val isMainUserTicket: Boolean,
    val hasNc: Boolean = false,
    var pdfUrl: String? = null,
    val pdfName: String? = null,
    val classId: String? = null,
    val classColor: String? = null,
    val hasUserFocus: Boolean = true,
    val justify_item_id: String?,
    val justify_item_desc: String?,
    val not_exec_comments: String?,
    val waiting_approve: Boolean? = null,
    val hasNotExecuted: Int? = null,
    val ticketEdiID: String? = null
) : MyActionsBase() {


    companion object {
        const val MY_ACTION_TYPE_TICKET = "TICKET"
        const val MY_ACTION_TYPE_TICKET_CACHE = "TICKET_CACHE"
        const val MY_ACTION_TYPE_SCHEDULE = "SCHEDULE"
        const val MY_ACTION_TYPE_FORM_AP = "FORM_AP"
        const val MY_ACTION_TYPE_FORM = "FORM"
    }

    var productCode: Int? = null
    var productId: String? = null
    var customFormDesc: String? = null
    var scheduleCustomFormData: String? = null
    var erroMsg: String? = null

    fun getSplippedPk(): List<String> {
        return processPk.split(".")
    }

    val containWaitingApproval = waiting_approve ?: false

    fun getFormattedSiteZoneDesc(): String? {
        return zoneDesc?.let {
            if (it.isNotEmpty()
                && !it.equals(siteDesc, true)) {
                "$siteDesc | $zoneDesc"
            } else {
                siteDesc
            }
        } ?: siteDesc
    }

    fun getAllFieldForFilter(): String {
        return "$processId|" +
                "$processStatusTrans|" +
                "$plannedDate|" +
                "$tagOperationDesc|" +
                "$productDesc|" +
                "$serialId|" +
                "$originDescriptor|" +
                "$processDesc|" +
                "$internalComments|" +
                "$focusStepDesc|" +
                "$siteDesc|" +
                "$classId|" +
                "$zoneDesc|" +
                "$serviceOrderCode|" +
                "$ticketEdiID"
                    .replace("null|", "")
                    .replace("null", "")
    }
}
