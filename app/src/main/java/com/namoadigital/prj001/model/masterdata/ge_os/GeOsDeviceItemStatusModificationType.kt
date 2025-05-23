package com.namoadigital.prj001.model.masterdata.ge_os

enum class GeOsDeviceItemStatusModificationType (val status: String) {
    ITEM("ITEM"),
    VERIFICATION_GROUP("VERIFICATION_GROUP");

    companion object {
        fun getModificationType(status: String?): GeOsDeviceItemStatusModificationType? {
            status?.let{
                return GeOsDeviceItemStatusModificationType.entries.find { it.status == status }
            }
            return null
        }
    }
}