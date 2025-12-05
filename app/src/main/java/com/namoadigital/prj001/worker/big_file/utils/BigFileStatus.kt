package com.namoadigital.prj001.worker.big_file.utils

enum class BigFileStatus {
    PENDING, DONE, NO_VALUE, NOT_FOUND, PROCESS, DOWNLOAD;

    companion object {
        fun fromString(value: String?): BigFileStatus? {
            return try {
                value?.let { valueOf(it.uppercase()) }
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        fun fromStringOrDefault(value: String?, default: BigFileStatus = NO_VALUE): BigFileStatus {
            return fromString(value) ?: default
        }
    }
}