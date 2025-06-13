package com.namoadigital.prj001.model.masterdata.ge_os

enum class ProcessVg {

    FORCE_EXECUTION_EXPIRED,
    BLOCK_EXECUTION;

    companion object {
        fun fromString(value: String?): ProcessVg? {
            return when (value) {
                "FORCE_EXECUTION_EXPIRED" -> FORCE_EXECUTION_EXPIRED
                "BLOCK_EXECUTION" -> BLOCK_EXECUTION
                else -> null
            }
        }

        fun isBlockExecution(value: String?): Boolean {
            return fromString(value) == BLOCK_EXECUTION
        }
    }
}