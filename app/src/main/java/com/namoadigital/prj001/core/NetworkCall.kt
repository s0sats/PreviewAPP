package com.namoadigital.prj001.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.legacy.content.WakefulBroadcastReceiver
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.data.remote.domain.ApiResponse
import com.namoadigital.prj001.dao.trip.FSTripDao
import com.namoadigital.prj001.model.Main_Header_Env
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripFull
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

typealias WsResult<T> = (IResult<T>) -> Unit


inline fun <reified WBR_CLASS : WakefulBroadcastReceiver> Context.sendToWebServiceReceiver(
    noinline networkReceiver: (() -> Bundle)? = null
) {
    Intent(this, WBR_CLASS::class.java).apply {
        networkReceiver?.let {
            putExtras(it.invoke())
        }

        this@sendToWebServiceReceiver.sendBroadcast(this)
    }
}

inline fun <reified API : ApiResponse<*>> Context.connectWS(
    url: String,
    model: Main_Header_Env,
    errorFeedback: Boolean = true,
    wsResult: WsResult<API>
) {
    var gson: Gson?
    gson = GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create()
    val jsonString = gson.toJson(model)

    runCatching {
        ToolBox_Con.connWebService(url, jsonString)
    }.fold(
        onSuccess = { success ->
            val type = object : TypeToken<API>() {}.type
            gson = GsonBuilder().serializeNulls().create()
            val response = gson?.fromJson<API>(success, type)!!
            val hmAuxTrans = loadNetworkTranslate()
            val serverCode = response.status?.code

            if (processWSCheckValidation(
                    response.validation,
                    response.error_msg,
                    response.link_url
                )
            ) return


            if (serverCode == 200) {
                wsResult(IResult.success(response))
                return
            }
            if (errorFeedback) {
                when (serverCode) {
                    500 -> {
                        ToolBox_Inf.registerException(
                            javaClass.name,
                            NetworkConnectionException(response.status?.serverMessage)
                        )
                        ToolBox.sendBCStatus(
                            this,
                            "ERROR_1",
                            response.status?.message ?: hmAuxTrans[NETWORK_GENERIC_ERROR],
                            "",
                            ""
                        )
                        return
                    }

                    412 -> {
                        val tripDao = FSTripDao(this)
                        val tripType = object : TypeToken<ApiResponse<FSTripFull>>() {}.type
                        Gson().fromJson<ApiResponse<FSTripFull>>(success, tripType).let {
                            tripDao.syncTripFull(it.data?.tripFull!!)
                        }
                        ToolBox.sendBCStatus(
                            this,
                            "ERROR_1",
                            response.status?.message,
                            FSTrip.TRIP_FULL_ERROR,
                            ""
                        )
                        return
                    }

                    406 -> {
                        ToolBox.sendBCStatus(
                            this,
                            "CUSTOM_ERROR",
                            response.status?.message,
                            "",
                            ""
                        )
                    }

                    else -> {
                        ToolBox_Inf.registerException(
                            javaClass.name,
                            NetworkConnectionException(response.status?.message)
                        )
                        val errorCode = 400..499
                        //
                        if (errorCode.contains(response.status?.code)) {
                            ToolBox.sendBCStatus(
                                this,
                                "ERROR_1",
                                response.status?.message,
                                "",
                                ""
                            )
                            return
                        }
                        //
                        ToolBox.sendBCStatus(
                            this,
                            "ERROR_1",
                            hmAuxTrans[NETWORK_GENERIC_ERROR],
                            "",
                            ""
                        )
                        return
                    }
                }
            } else {
                val gsonReturn = GsonBuilder().serializeNulls().create()
                val responseError = gsonReturn.toJson(response)
                wsResult(IResult.error(responseError))
            }
        },
        onFailure = { ex ->
            wsResult(IResult.failed(ex))
            wsExceptionTreatment(ex, errorFeedback)
        }
    )
}


fun Context.wsExceptionTreatment(ex: Throwable, errorFeedback: Boolean = true) {
    ToolBox_Inf.wsExceptionTreatment(this, Exception(ex)).let { string ->
        ToolBox_Inf.registerException(javaClass.name, Exception(ex))
        if (errorFeedback) {
            ToolBox.sendBCStatus(
                this,
                "ERROR_1",
                string.toString(),
                "",
                "0"
            )
        }
    }
}


fun Context.processWSCheckValidation(
    validation: String?,
    errorMsg: String?,
    url: String?
): Boolean {
    return !ToolBox_Inf.processWSCheckValidation(
        this,
        validation ?: "",
        errorMsg ?: "",
        url ?: "",
        1,
        1
    ) || !ToolBox_Inf.processoOthersError(
        this,
        resources.getString(R.string.generic_error_lbl),
        errorMsg ?: ""
    )
}

fun Context.loadNetworkTranslate(): HMAux {
    listOf(
        NETWORK_GENERIC_ERROR,
        DB_TRANSACTION_ERROR_LBL
    ).let {
        return ToolBox_Inf.setLanguage(
            this,
            Constant.APP_MODULE,
            ToolBox_Inf.getResourceCode(
                this,
                Constant.APP_MODULE,
                "0"
            ),
            ToolBox_Con.getPreference_Translate_Code(this),
            it
        )
    }
}

const val NETWORK_GENERIC_ERROR = "generic_error_lbl"
const val DB_TRANSACTION_ERROR_LBL = "db_transaction_error_lbl"