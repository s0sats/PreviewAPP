package com.namoadigital.prj001.core.trip.base

import android.content.Context
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.NETWORK_GENERIC_ERROR
import com.namoadigital.prj001.core.loadNetworkTranslate
import com.namoadigital.prj001.extensions.loadGenericTranslation
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Inf

abstract class BaseTripRepository constructor(
    private val context: Context
) {
    protected val networkTranslate by lazy {
        context.loadNetworkTranslate()
    }

    protected val genericTranslate by lazy {
        context.loadGenericTranslation()
    }

    fun handleNetworkError(throwable: Throwable?, context: Context): IResult<Unit> {
        throwable?.let { error ->
            val errorMsg = if (error is Exception) {
                ToolBox_Inf.wsExceptionTreatment(
                    context,
                    error
                ).toString()
            } else {
                ""
            }

            return IResult.error(
                errorMsg.ifEmpty { networkTranslate[NETWORK_GENERIC_ERROR]!! },
                NetworkConnectionException(networkTranslate[NETWORK_GENERIC_ERROR])
            )
        }

        return IResult.success(Unit)
    }

}