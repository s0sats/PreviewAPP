package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FSEventType(
    @Expose @SerializedName("customer_code") var customerCode: Long,
    @Expose @SerializedName("event_type_code") var eventTypeCode: Int,
    @Expose @SerializedName("event_type_desc") var eventTypeDesc: String,
    @Expose @SerializedName("conf_cost") var confCost: String,
    @Expose @SerializedName("conf_comments") var confComments: String,
    @Expose @SerializedName("conf_photo") var confPhoto: String,
    @Expose @SerializedName("wait_allowed") var waitAllowed: Int,
    @Expose @SerializedName("wait_max_minutes") var waitMaxMinutes: Int?,
){

    val isRequiredCost = confCost == CONFIGURE_FIELD_REQUIRED
    val isRequiredComment = confComments == CONFIGURE_FIELD_REQUIRED
    val isRequiredPhoto = confPhoto == CONFIGURE_FIELD_REQUIRED
    val isWaitAllowed = waitAllowed == 1
    val hideCost = confCost == CONFIGURE_FIELD_HIDE
    val hideComment = confComments == CONFIGURE_FIELD_HIDE
    val hidePhoto = confPhoto == CONFIGURE_FIELD_HIDE

    companion object{
        const val CONFIGURE_FIELD_HIDE = "HIDE"
        const val CONFIGURE_FIELD_REQUIRED = "REQUIRED"
        const val CONFIGURE_FIELD_OPTIONAL = "OPTIONAL"
    }
}


