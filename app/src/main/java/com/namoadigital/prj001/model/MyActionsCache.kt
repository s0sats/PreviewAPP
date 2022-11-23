package com.namoadigital.prj001.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.util.ToolBox_Con

class MyActionsCache(
    @SerializedName("action_type") val actionType: String,
    val processPk: String,
    @SerializedName("process_id") val processId: String?,
    @SerializedName("process_status") val processStatus: String,
    val processStatusTrans: String?,
    @DrawableRes val processLeftIcon: Int?,
    @DrawableRes val processRightIcon: Int?,
    @SerializedName("planned_date") val plannedDate: String?,
    @SerializedName("tag_operational_code") val tag_operational_code: Int,
    @SerializedName("tag_operational_id") val tag_operational_id: String,
    @SerializedName("tag_operational_desc") val tagOperationDesc: String,
    @SerializedName("origin_desc") val originDescriptor: String,
    @SerializedName("process_desc") val processDesc: String,
    @SerializedName("comments") val internalComments: String?,
    @SerializedName("focus_step_desc") val focusStepDesc: String?,
    @SerializedName("site_code") val siteCode: Int? = null,
    @SerializedName("site_desc") val siteDesc: String?,
    @SerializedName("zone_desc") val zoneDesc: String?,
    @SerializedName("done_date") val doneDate: String?,
    @SerializedName("order_by") val orderBy: String,
    @SerializedName("ticket_origin_type") val ticketOriginType: String?,
    val highlightItem: Boolean,
    val periodStarted: Boolean,
    val lateItem: Boolean,
    val isLastSelectedItem: Boolean,
    @SerializedName("main_user") val mainUser: Int?,
    @SerializedName("user_focus") val userFocus: Int,
    @SerializedName("has_Nc") val hasNc: Int,
    @SerializedName("pdf_url") val pdfUrl: Int,
    @SerializedName("pdf_name") val pdfName: Int
) {
    fun toMyActions(context: Context): MyActions {
        val processLeftIcon = 0
        val processRightIcon = 0
        var  isMainUserTicket = false
        mainUser?.let {
            isMainUserTicket = mainUser == ToolBox_Con.getPreference_User_Code(context).toInt()
        }
        //
        return MyActions(
            actionType,
            processId ?: "",
            processId,
            processStatus,
            processStatusTrans,
            processLeftIcon,
            processRightIcon,
            plannedDate ?: "",
            tagOperationDesc,
            processDesc,
            null,
            originDescriptor,
            processDesc,
            internalComments,
            focusStepDesc,
            siteCode,
            siteDesc,
            zoneDesc,
            null,
            doneDate ?: "",
            orderBy,
            ticketOriginType ?: "",
            "highlightItem" == "highlightItem",
            periodStarted,
            lateItem,
            isLastSelectedItem,
            isMainUserTicket
        )
    }
}