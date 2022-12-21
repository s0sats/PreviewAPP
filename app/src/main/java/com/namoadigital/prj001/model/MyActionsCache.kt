package com.namoadigital.prj001.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.MyActions.Companion.MY_ACTION_TYPE_FORM
import com.namoadigital.prj001.model.MyActions.Companion.MY_ACTION_TYPE_SCHEDULE
import com.namoadigital.prj001.model.MyActions.Companion.MY_ACTION_TYPE_TICKET_CACHE
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class MyActionsCache(
    @SerializedName("action_type") val actionType: String,
    @SerializedName("data_pk") val processPk: String,
    @SerializedName("process_id") val processId: String?,
    @SerializedName("process_status") var processStatus: String,
    val processStatusTrans: String?,
    @DrawableRes val processLeftIcon: Int?,
    @DrawableRes val processRightIcon: Int?,
    @SerializedName("planned_date_start") val plannedDateStart: String?,
    @SerializedName("planned_date_end") val plannedDateEnd: String?,
    @SerializedName("tag_operational_code") val tag_operational_code: Int,
    @SerializedName("tag_operational_id") val tag_operational_id: String,
    @SerializedName("tag_operational_desc") val tagOperationDesc: String,
    @SerializedName("origin_desc") val originDescriptor: String?,
    @SerializedName("process_desc") val processDesc: String,
    @SerializedName("comments") val internalComments: String?,
    @SerializedName("focus_step_desc") val focusStepDesc: String?,
    @SerializedName("site_code") val siteCode: Int? = null,
    @SerializedName("site_desc") val siteDesc: String?,
    @SerializedName("zone_desc") val zoneDesc: String?,
    @SerializedName("process_date_start") val doneDateStart: String?,
    @SerializedName("process_date_end") val doneDateEnd: String?,
    @SerializedName("data_order") val orderBy: String,
    @SerializedName("data_type") val data_type: String,
    @SerializedName("ticket_origin_type") val ticketOriginType: String?,
    @SerializedName("ticket_scn") val ticketScn: Int?,
    val highlightItem: Boolean,
    val periodStarted: Boolean,
    val lateItem: Boolean,
    val isLastSelectedItem: Boolean,
    @SerializedName("main_user") val mainUser: Int?,
    @SerializedName("user_focus") val userFocus: Int?,
    @SerializedName("has_nc") val hasNc: Int?,
    @SerializedName("pdf_url") var pdfUrl: String?,
    @SerializedName("pdf_name") var pdfName: String?,
    @SerializedName("ticket_class_id") val ticketClassId: String?,
    @SerializedName("ticket_class_color") val ticketClassColor: String?,
    @SerializedName("justify_item_id") val justify_item_id: String?,
    @SerializedName("justify_item_desc") val justify_item_desc: String?,
    @SerializedName("not_executed_comments") val not_executed_comments: String?,
    ) {
    fun toMyActions(context: Context, productCode: Int, serialId: String): MyActions {
        val processLeftIcon = getLeftIcon()
        val processMidIcon = getMidIcon()
        val processRightIcon = getRightIcon()
        //
        var type = actionType
        if (type == MyActions.MY_ACTION_TYPE_TICKET) {
            type = MY_ACTION_TYPE_TICKET_CACHE
        }
        var formattedPlannedDate: String? = null
        var formattedDoneDate: String? = null
        //
        if (plannedDateStart != null && plannedDateEnd != null) {
            formattedPlannedDate = ToolBox_Inf.getMyActionStartEndDateFormated(
                context,
                plannedDateStart,
                plannedDateEnd
            )
        }
        //
        val waitingApprove = processStatus == ConstantBaseApp.SYS_STATUS_WAITING_APPROVAL
        //
        if (doneDateStart != null && doneDateEnd != null) {

            when(type){
                MY_ACTION_TYPE_TICKET_CACHE ->{
                    if(processStatus == ConstantBaseApp.SYS_STATUS_DONE) {
                        formattedPlannedDate = null
                    }

                    formattedDoneDate =ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(doneDateEnd),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                }
                MY_ACTION_TYPE_SCHEDULE ->{
                    formattedDoneDate =ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(doneDateEnd),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                }
                MY_ACTION_TYPE_FORM ->{
                    //
                    if(waitingApprove){
                        formattedPlannedDate = ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(doneDateEnd),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                        formattedDoneDate = null
                    }else {
                        formattedPlannedDate = ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(doneDateStart),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                        //
                        formattedDoneDate =ToolBox_Inf.millisecondsToString(
                            ToolBox_Inf.dateToMilliseconds(doneDateEnd),
                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                        )
                    }

                }
                else ->{
                    formattedDoneDate =
                        ToolBox_Inf.getMyActionStartEndDateFormated(context, doneDateStart, doneDateEnd)
                }
            }
        }
        //
        var myActions = MyActions(
            type,
            processId ?: "",
            processId,
            processStatus,
            ConstantBaseApp.HMAUX_TRANS_LIB[processStatusTrans],
            processLeftIcon,
            processMidIcon,
            processRightIcon,
            formattedPlannedDate ?: "",
            tagOperationDesc,
            "",
            serialId,
            originDescriptor ?: "",
            processDesc,
            internalComments,
            focusStepDesc,
            siteCode,
            siteDesc,
            zoneDesc,
            null,
            formattedDoneDate,
            orderBy,
            ticketOriginType ?: "",
            ticketScn,
            false,
            periodStarted,
            lateItem,
            isLastSelectedItem,
            false,
            hasNc == 1,
            pdfUrl,
            pdfName,
            ticketClassId,
            ticketClassColor,
            false,
            justify_item_id,
            justify_item_desc,
            not_executed_comments,
            waitingApprove
        )
        myActions.productCode = productCode
        return myActions
    }

    private fun getMidIcon(): Int? {
        return if(actionType == MyActions.MY_ACTION_TYPE_FORM){
            val pdfFile = File(ConstantBaseApp.CACHE_PATH + "/" + pdfName)
            if(pdfFile.exists() && pdfFile.isFile){
                R.drawable.ic_baseline_cloud_done_24_blue
            }else {
                R.drawable.ic_baseline_cloud_download_24_gray
            }
        }else{
            if(actionType == MyActions.MY_ACTION_TYPE_SCHEDULE
                && processStatus ==  ConstantBaseApp.SYS_STATUS_NOT_EXECUTED){
                null
            }else {
                if(actionType == MyActions.MY_ACTION_TYPE_SCHEDULE && pdfName.isNullOrEmpty() && pdfUrl.isNullOrEmpty() && processStatus == ConstantBaseApp.SYS_STATUS_DONE){
                    R.drawable.ic_baseline_cloud_done_24_blue
                }else {
                    R.drawable.ic_baseline_cloud_download_24_gray
                }
            }
        }
    }

    private fun getLeftIcon(): Int? {

        return if(hasNc == 1){
            R.drawable.ic_baseline_report_24_yellow
        }else{
            null
        }
    }

    private fun getRightIcon(): Int {
        return when(processStatus){
            ConstantBaseApp.SYS_STATUS_DONE ->{
                R.drawable.ic_baseline_check_circle_24
            }
            ConstantBaseApp.SYS_STATUS_NOT_EXECUTED ->{
                R.drawable.ic_baseline_cancel_24
            }
            else -> {
                R.drawable.ic_unfocus_person_off
            }
        }
    }
}