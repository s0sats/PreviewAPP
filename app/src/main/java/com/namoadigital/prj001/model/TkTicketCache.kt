package com.namoadigital.prj001.model

import android.content.Context
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.ToolBox_Inf

class TkTicketCache(
        var customer_code: Int,
        var ticket_prefix: Int,
        var ticket_code: Int,
        var scn: Int,
        var user_level_min: Int,
        var ticket_id: String,
        var tag_operational_code: Int,
        var tag_operational_id: String,
        var tag_operational_desc: String,
        var type_code: Int,
        var type_id: String,
        var type_desc: String,
        var user_focus: Int,
        var order_by: Long,
        var client_code: Int?,
        var client_id: String?,
        var client_name: String?,
        var contract_code: Int?,
        var contract_id: String?,
        var contract_desc: String?,
        var open_site_code: Int,
        var open_site_desc: String,
        var open_product_code: Int,
        var open_product_desc: String,
        var open_serial_id: String,
        var current_step_order: Int?,
        var ticket_status: String,
        var origin_desc: String,
        var step_desc: String?,
        var forecast_start: String?,
        var forecast_end: String?,
        var step_count: Int,
        var step_order_seq: Int?
){
    fun toMyActionsObj(context: Context): MyActions{
        return MyActions(
                ticket_id,
                ticket_status,
                null,
                R.drawable.ic_baseline_cloud_download_24,
                ToolBox_Inf.getStepStartEndDateFormated(context, forecast_start, forecast_end),
                tag_operational_desc,
                open_product_desc,
                open_serial_id,
                origin_desc,
                processDesc = type_desc,
                step_desc,
                open_site_desc,
                client_id?.let { "$client_id - $client_name" },
                contract_id?.let { "$contract_id - $contract_desc" },
                null,
                null,
                ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(forecast_start),
                        "yyyyMMddHHmm"
                )
        )
    }
}