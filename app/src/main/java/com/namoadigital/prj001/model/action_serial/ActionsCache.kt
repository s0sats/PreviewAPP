package com.namoadigital.prj001.model.action_serial

import com.google.gson.annotations.SerializedName

data class ActionsCache(
    @SerializedName("action_type")
    var actionType: String = "",
    @SerializedName("process_id")
    var processPk: String = "",
    @SerializedName("process_id")
    var processId: String? = null,
    @SerializedName("process_status")
    var processStatus: String = "",
    @SerializedName("planned_date")
    var plannedDate: String? = null,
    @SerializedName("tag_operational_code")
    var tag_operational_code: Int = -1,
    @SerializedName("tag_operational_id")
    var tag_operational_id: String = "",
    @SerializedName("tag_operational_desc")
    var tagOperationDesc: String = "",
    @SerializedName("origin_desc")
    var originDescriptor: String = "",
    @SerializedName("process_desc")
    var processDesc: String = "",
    @SerializedName("comments")
    var internalComments: String? = null,
    @SerializedName("focus_step_desc")
    var focusStepDesc: String? = null,
    @SerializedName("site_code")
    var siteCode: Int? = null,
    @SerializedName("site_desc")
    var siteDesc: String? = null,
    @SerializedName("zone_desc")
    var zoneDesc: String? = null,
    @SerializedName("done_date")
    var doneDate: String? = null,
    @SerializedName("order_by")
    var orderBy: String = "",
    @SerializedName("ticket_origin_type")
    var ticketOriginType: String? = null,
    @SerializedName("main_user")
    var isMainUserTicket: Int? = null,
    @SerializedName("user_focus")
    var userFocus: Int = -1,
    @SerializedName("has_Nc")
    var hasNc: Int = -1,
    @SerializedName("pdf_url")
    var pdfUrl: Int = -1,
    @SerializedName("pdf_name")
    var pdfName: Int = -1,
)
