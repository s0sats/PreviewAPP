package com.namoadigital.prj001.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class TkTicketCache(
        @SerializedName("customer_code") var customer_code: Int,
        @SerializedName("ticket_prefix") var ticket_prefix: Int,
        @SerializedName("ticket_code") var ticket_code: Int,
        @SerializedName("scn") var scn: Int,
        @SerializedName("user_level_min") var user_level_min: Int,
        @SerializedName("ticket_id") var ticket_id: String,
        @SerializedName("tag_operational_code") var tag_operational_code: Int,
        @SerializedName("tag_operational_id") var tag_operational_id: String,
        @SerializedName("tag_operational_desc") var tag_operational_desc: String,
        @SerializedName("type_code") var type_code: Int,
        @SerializedName("type_id") var type_id: String,
        @SerializedName("type_desc") var type_desc: String,
        @SerializedName("user_focus") var user_focus: Int,
        @SerializedName("main_user") var main_user: Int?,
        @SerializedName("order_by") var order_by: Long,
        @SerializedName("client_code") var client_code: Int?,
        @SerializedName("client_id") var client_id: String?,
        @SerializedName("client_name") var client_name: String?,
        @SerializedName("contract_code") var contract_code: Int?,
        @SerializedName("contract_id") var contract_id: String?,
        @SerializedName("contract_desc") var contract_desc: String?,
        @SerializedName("open_site_code") var open_site_code: Int,
        @SerializedName("open_site_desc") var open_site_desc: String,
        @SerializedName("open_zone_code") var open_zone_code: Int?,
        @SerializedName("open_zone_desc") var open_zone_desc: String?,
        @SerializedName("open_operation_code") var open_operation_code: Int,
        @SerializedName("open_operation_desc") var open_operation_desc: String,
        @SerializedName("open_product_code") var open_product_code: Int,
        @SerializedName("open_product_desc") var open_product_desc: String,
        @SerializedName("open_serial_id") var open_serial_id: String,
        @SerializedName("current_step_order") var current_step_order: Int?,
        @SerializedName("ticket_status") var ticket_status: String,
        @SerializedName("origin_type") var origin_type: String,
        @SerializedName("origin_desc") var origin_desc: String,
        @SerializedName("internal_comments") var internal_comments: String?,
        @SerializedName("step_desc") var step_desc: String?,
        @SerializedName("forecast_start") var forecast_start: String?,
        @SerializedName("forecast_end") var forecast_end: String?,
        @SerializedName("step_count") var step_count: Int,
        @SerializedName("step_order_seq") var step_order_seq: Int?,
        @SerializedName("class_code")  var class_code: Int? = null,
        @SerializedName("class_id") var class_id: String? = null,
        @SerializedName("class_color")  var class_color: String? = null,
        @SerializedName("class_available")  var class_available: Int? = null,
){
    fun toMyActionsObj(context: Context, lastSelectedActionPk: String?): MyActions{
        val statusTrad = ConstantBaseApp.HMAUX_TRANS_LIB?.get(ticket_status) ?: ticket_status
        //
        val formattedZone = ToolBox_Inf.getProductSerialZone(
                context,
                open_site_code,
                open_zone_desc,
                MD_Product_SerialDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                ),
                open_product_code.toLong(),
                open_serial_id
            )
        //
        var rightIcon: Int? = null
        if (main_user?.toString()?.equals(ToolBox_Con.getPreference_User_Code(context)) ?: false) {
            rightIcon = R.drawable.ic_baseline_person_24_secondary60
        }else{
            rightIcon = R.drawable.ic_baseline_group_24
        }
        val processPk = "$ticket_prefix.$ticket_code.$scn"
        val myActions = MyActions(
            MyActions.MY_ACTION_TYPE_TICKET_CACHE,
            processPk,
            ticket_id,
            ticket_status,
            statusTrad,
            null,
            R.drawable.ic_baseline_cloud_download_24_gray,
            rightIcon,
            ToolBox_Inf.getMyActionStartEndDateFormated(context, forecast_start, forecast_end),
            tag_operational_desc,
            open_product_desc,
            open_serial_id,
            origin_desc,
            type_desc,
            internal_comments,
            step_desc,
            open_site_code,
            open_site_desc,
            formattedZone,
            null,
            null,
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(forecast_start),
                "yyyyMMddHHmm"
            ),
            origin_type,
            scn,
            false,
            ToolBox_Inf.isItemLate(forecast_start),
            ToolBox_Inf.isItemLate(forecast_end),
            processPk == lastSelectedActionPk,
            main_user?.toString()?.equals(ToolBox_Con.getPreference_User_Code(context)) ?: false,
            false,
            null,
            null,
            class_id,
            class_color,
            true,
            null,
            null
        )
        myActions.productCode = open_product_code
        return myActions
    }
}