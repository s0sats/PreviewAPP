package com.namoadigital.prj001.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class MyActionsCache(
    @SerializedName("action_type") val actionType: String,
    @SerializedName("data_pk") val processPk: String,
    @SerializedName("process_id") val processId: String?,
    @SerializedName("process_status") val processStatus: String,
    val processStatusTrans: String?,
    @DrawableRes val processLeftIcon: Int?,
    @DrawableRes val processRightIcon: Int?,
    @SerializedName("planned_date") val plannedDate: String?,
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
    @SerializedName("process_date_end") val doneDate: String?,
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
    @SerializedName("pdf_name") val pdfName: String
) {
    fun toMyActions(context: Context): MyActions {
        val processLeftIcon = getLeftIcon()
        val processMidIcon = getMidIcon()
        val processRightIcon = getRightIcon()
        //
        return MyActions(
            actionType,
            processId ?: "",
            processId,
            processStatus,
            ConstantBaseApp.HMAUX_TRANS_LIB[processStatusTrans],
            processLeftIcon,
            processMidIcon,
            processRightIcon,
            plannedDate ?: "",
            tagOperationDesc,
            processDesc,
            null,
            originDescriptor?:"",
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
            ticketScn,
            "highlightItem" == "highlightItem",
            periodStarted,
            lateItem,
            isLastSelectedItem,
            false,
            hasNc == 1,
            pdfUrl,
            pdfName,
            null,
            null
        )
    }

    private fun getMidIcon(): Int? {
        return if(actionType == ConstantBaseApp.FCM_MODULE_SCHEDULE
            && processStatus == ConstantBaseApp.SYS_STATUS_NOT_EXECUTED){
            null
        }else{
            R.drawable.ic_baseline_cloud_download_24_gray
        }
    }

    private fun getLeftIcon(): Int? {

        return if(hasNc == 0){
            null
        }else{
            R.drawable.ic_baseline_report_24
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