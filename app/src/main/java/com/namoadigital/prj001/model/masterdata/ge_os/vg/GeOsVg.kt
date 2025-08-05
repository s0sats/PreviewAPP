package com.namoadigital.prj001.model.masterdata.ge_os.vg

import com.google.gson.annotations.SerializedName

data class GeOsVg(
    @SerializedName("customer_code") var customerCode: Long,
    @SerializedName("custom_form_type") val customFormType: Int,
    @SerializedName("custom_form_code") val customFormCode: Int,
    @SerializedName("custom_form_version") val customFormVersion: Int,
    @SerializedName("custom_form_data") var customFormData: Int,
    @SerializedName("product_code") var productCode: Int,
    @SerializedName("serial_code") var serialCode: Int,
    @SerializedName("vg_code") var vgCode: Int,
    @SerializedName("vg_id") var vgId: String? = null,
    @SerializedName("vg_desc") var vgDesc: String? = null,
    @SerializedName("next_cycle_measure") val nextCycleMeasure: Float? = null,
    @SerializedName("next_cycle_measure_date") val nextCycleMeasureDate: String? = null,
    @SerializedName("next_cycle_limit_date") val nextCycleLimitDate: String? = null,
    @SerializedName("last_execution_measure") val lastExecutionMeasure: Float? = null,
    @SerializedName("last_execution_date") val lastExecutionDate: String? = null,
    @SerializedName("vg_status") var vgStatus: String = "",
    @SerializedName("target_date") var targetDate: String? = null,
    @SerializedName("manual_date") val manualDate: String? = null,
    @SerializedName("value_sufix") val valueSuffix: String? = null,
    @SerializedName("restriction_decimal") val restriction_decimal: String? = null,
    @SerializedName("partitioned_ticket_prefix") val partitionedTicketPrefix: Int? = null,
    @SerializedName("partitioned_ticket_code") val partitionedTicketCode: Int? = null,
    @SerializedName("partitioned_user") val partitionedUser: String? = null,
    @SerializedName("partitioned_execution") val partitionedExecution: Int? = null,
    @SerializedName("isActive") var isActive: Int = 0,
    @SerializedName("hasExpired") var hasExpired: Int = 0,
    @SerializedName("exec_only_preventive") var execOnlyPreventive: Int = 0,
) {

    var ticket = if (partitionedTicketPrefix != null && partitionedTicketCode != null) {
        "$partitionedTicketPrefix.$partitionedTicketCode"
    } else null

    fun isValidTickets(
        prefix: Int?,
        code: Int?,
    ): Boolean {
        if (prefix == null || code == null) return false
        if (partitionedTicketPrefix == null || partitionedTicketCode == null) return false
        return true
    }

    fun isSameTicket(
        prefix: Int?,
        code: Int?,
    ): Boolean {
        if (prefix == null || code == null) return false
        if (partitionedTicketPrefix == null || partitionedTicketCode == null) return false
        return prefix == partitionedTicketPrefix && code == partitionedTicketCode
    }

    fun isExpired() = hasExpired == 1
    fun isActive() = isActive == 1
    fun isExecOnlyPreventive() = execOnlyPreventive == 1

    fun hasPartition() = partitionedExecution == 1
}