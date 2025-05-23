package com.namoadigital.prj001.extensions.coroutines

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import kotlin.reflect.KClass

fun <T> Flow<IResult<T>>.namoaCatch(
    local: String,
) = apply {
    this.catch { e ->
        ToolBox_Inf.registerException(local, IOException(e.message))
        emit(loading(false))
        emit(failed(e))
    }
}

fun <T> Flow<IResult<T>>.namoaCatch(
    clazz: KClass<*>,
) = apply {
    this.catch { e ->
        ToolBox_Inf.registerException(clazz.simpleName, IOException(e.message))
        emit(loading(false))
        emit(failed(e))
    }
}


fun <T> Flow<IResult<T>>.flowCatch(
    local: String,
) = this.catch { e ->
    ToolBox_Inf.registerException(local, IOException(e.message))
    emit(loading(false))
    emit(failed(e))
}

fun <T> Flow<IResult<T>>.dispatchersIO() = this.flowOn(Dispatchers.IO)
