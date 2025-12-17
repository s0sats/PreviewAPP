package com.namoadigital.prj001.model.masterdata.ge_os

enum class ProcessVg {

    FORCE_EXECUTION_EXPIRED,
    FORCE_EXECUTION_ALL_GROUPS,
    BLOCK_EXECUTION;

    companion object {
        fun fromString(value: String?): ProcessVg? {
            return when (value) {
                "FORCE_EXECUTION_EXPIRED" -> FORCE_EXECUTION_EXPIRED
                "FORCE_EXECUTION_ALL_GROUPS" -> FORCE_EXECUTION_ALL_GROUPS
                "BLOCK_EXECUTION" -> BLOCK_EXECUTION
                else -> null
            }
        }

        fun isBlockExecution(value: String?): Boolean {
            return fromString(value) == BLOCK_EXECUTION
        }
        fun isForceExecutionAllGroups(value: String?): Boolean {
            return fromString(value) == FORCE_EXECUTION_ALL_GROUPS
        }
        fun isForceExecutionAllGroups(value: ProcessVg?): Boolean {
            return value == FORCE_EXECUTION_ALL_GROUPS
        }

    }
}