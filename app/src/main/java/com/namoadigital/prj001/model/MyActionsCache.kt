package com.namoadigital.prj001.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.model.MyActions.Companion.MY_ACTION_TYPE_TICKET_CACHE
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class MyActionsCache(
    @SerializedName("action_type") val actionType: String,
    @SerializedName("data_pk") val processPk: String,
    @SerializedName("process_id") val processId: String?,
    @SerializedName("process_status") val processStatus: String,
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
    @SerializedName("ticket_origin_type") val ticketOriginType: String?,
    @SerializedName("ticket_scn") val ticketScn: Int?,
    val highlightItem: Boolean,
    val periodStarted: Boolean,
    val lateItem: Boolean,
    val isLastSelectedItem: Boolean,
    @SerializedName("main_user") val mainUser: Int?,
    @SerializedName("user_focus") val userFocus: Int,
    @SerializedName("has_Nc") val hasNc: Int,
    @SerializedName("pdf_url") val pdfUrl: String,
    @SerializedName("pdf_name") val pdfName: String,
    @SerializedName("ticket_class_id") val ticketClassId: String,
    @SerializedName("ticket_class_color") val ticketClassColor: String
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
        var formattedPlannedDate:String? = null
        var formattedDoneDate:String? = null
        //
        if (plannedDateStart != null && plannedDateEnd != null){
            formattedPlannedDate =ToolBox_Inf.getMyActionStartEndDateFormated(context, plannedDateStart, plannedDateEnd)
        }
        //
        if (doneDateStart != null && doneDateEnd != null){
            formattedDoneDate = ToolBox_Inf.getMyActionStartEndDateFormated(context, doneDateStart,doneDateEnd)
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
            processDesc,
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
            "highlightItem" == "highlightItem",
            periodStarted,
            lateItem,
            isLastSelectedItem,
            false,
            hasNc == 1,
            pdfUrl,
            pdfName,
            ticketClassId,
            ticketClassColor,
            false
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
            R.drawable.ic_baseline_cloud_download_24_gray
        }
    }

    private fun getLeftIcon(): Int? {

        return if(hasNc == 0){
            null
        }else{
            R.drawable.ic_baseline_report_24_yellow
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