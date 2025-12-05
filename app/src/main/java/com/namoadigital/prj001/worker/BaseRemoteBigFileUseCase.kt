package com.namoadigital.prj001.worker

import android.content.Context
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus

abstract class BaseRemoteBigFileUseCase(
    val context: Context,
) {
    //
    data class DataInput(
        val bigFile: BigFile
    )
    //
    sealed class DataOutput {
        data class Error(val errorMsg: String? = null) : DataOutput()
        data class Success(val bigFile: BigFile) : DataOutput()
    }

    //
    abstract suspend fun invoke(inputData: DataInput): DataOutput

    //
    fun checkValidation(validation: String?): Boolean {
        when (validation) {
            "VERSION_ERRO",
            "VERSION_INVALID",
            "EXPIRED",
            "LOGIN_ERRO",
            "USER_INVALID",
            "USER_CANCELLED",
            "SESSION_NOT_FOUND",
            "USER_BLOCKED",
            "CREATE_SESSION_ABORT",
            "LICENSE_QTY_INVALID",
            "PARAMETERS_ERROR",
            "CUSTOMER_IP_REQUIRED",
            "CUSTOMER_IP_RESTRICTION",
            null,
                -> {
                ToolBox_Con.setPreference_Status_Login(context,Constant.LOGIN_STATUS_SESSION_NOT_FOUND);
                return false
            }
        }

        return true
    }

    //
    fun formatErrorMsg(msg: String): DataOutput {
        return DataOutput.Error(
            msg
        )
    }
    //
    fun updateBigFileStatus(
        fileTypeTicket: String,
        status: BigFileStatus
    ): BigFile {
        val bigFilePreferenceManager = BigFilePreferenceManager(context, fileTypeTicket)
        val ticketBigFile = bigFilePreferenceManager.getBigFile()
        val resultBigFile = ticketBigFile.copy(fileStatus = status.toString())
        bigFilePreferenceManager.saveBigFile(resultBigFile)
        return resultBigFile
    }
}
