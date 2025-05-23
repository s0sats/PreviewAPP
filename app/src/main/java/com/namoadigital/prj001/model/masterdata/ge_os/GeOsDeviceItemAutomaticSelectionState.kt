package com.namoadigital.prj001.model.masterdata.ge_os

enum class GeOsDeviceItemAutomaticSelectionState(val status: String) {
    EXPIRED("EXPIRED"),
    PARTITIONED_FORCED("PARTITIONED_FORCED"),
    PARTITIONED_BLOCKED("PARTITIONED_BLOCKED"),
    EXPIRED_FORCED("EXPIRED_FORCED");
    companion object {
        fun getAutomaticSelection(status: String?): GeOsDeviceItemAutomaticSelectionState? {
            status?.let{
                return GeOsDeviceItemAutomaticSelectionState.entries.find { it.status == status }
            }
            return null
        }
    }
}