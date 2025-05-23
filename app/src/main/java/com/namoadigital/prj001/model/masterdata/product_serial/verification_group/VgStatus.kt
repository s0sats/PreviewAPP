package com.namoadigital.prj001.model.masterdata.product_serial.verification_group

enum class VgStatus(val status: String) {
    NORMAL("NORMAL"),
    LIMIT_DATE_REACHED("LIMIT_DATE_REACHED"),
    PROJECTED_DATE_REACHED("PROJECTED_DATE_REACHED"),
    MEASURE_ALERT("MEASURE_ALERT"),
    MANUALLY_FORCED_DATE("MANUALLY_FORCED_DATE");

    companion object {
        fun fromStatus(status: String?): VgStatus? {
            return entries.find { it.status == status }
        }
    }
}