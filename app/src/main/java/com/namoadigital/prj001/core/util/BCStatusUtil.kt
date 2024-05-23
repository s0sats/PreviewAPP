package com.namoadigital.prj001.core.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp

fun Context.sendBCStatus(type: WsTypeStatus) {

    when (type) {

        is WsTypeStatus.CLOSE_ACT -> {
            ToolBox.sendBCStatus(
                this,
                type.type,
                type.value,
                type.hmAux,
                type.response,
                type.required
            )
        }

        is WsTypeStatus.UPDATE_DIALOG_MESSAGE -> {
            ToolBox.sendBCStatus(
                this,
                type.type,
                type.message,
                type.hmAux,
                type.response,
                type.required
            )
        }

        is WsTypeStatus.ERROR -> {
            ToolBox.sendBCStatus(
                this,
                type.type,
                type.value,
                type.hmAux,
                type.response,
                type.required
            )
        }

        is WsTypeStatus.CUSTOM_ERROR -> {
            ToolBox.sendBCStatus(
                this,
                type.type,
                type.message,
                type.hmAux,
                type.response,
                type.required
            )
        }

        is WsTypeStatus.FCMStatus -> {
            Intent().also {
                it.action = Constant.WS_FCM
                it.addCategory(Intent.CATEGORY_DEFAULT)
                it.putExtra(ConstantBaseApp.SW_TYPE, type.moduleType)
                LocalBroadcastManager.getInstance(this).sendBroadcast(it)
            }
        }

    }

}


sealed class WsTypeStatus(
    val type: String,
    open val response: String? = null,
    open val value: String? = null,
    open val hmAux: HMAux?  = null,
    open val required: String? = null
) {

    data class CLOSE_ACT(
        override val response: String?,
        override val hmAux: HMAux = HMAux(),
        override val value: String = "",
        override val required: String = "0"
    ) : WsTypeStatus("CLOSE_ACT", response, value, hmAux, required)

    data class UPDATE_DIALOG_MESSAGE(
        val message: String?,
        override val hmAux: HMAux = HMAux(),
        override val required: String = ""
    ) : WsTypeStatus("STATUS", message, "", hmAux, required)

    data class ERROR(
        val message: String?,
        override val hmAux: HMAux = HMAux(),
        override val value: String = "",
        override val required: String = ""
    ) : WsTypeStatus("ERROR_1", message, value, hmAux, required)

    data class CUSTOM_ERROR(
        val message: String?,
        override val hmAux: HMAux = HMAux(),
        override val value: String = "",
        override val required: String = ""
    ) : WsTypeStatus("CUSTOM_ERROR", message, value, hmAux, required)

    data class FCMStatus(
        val moduleType: String,
    ) : WsTypeStatus(moduleType)

}