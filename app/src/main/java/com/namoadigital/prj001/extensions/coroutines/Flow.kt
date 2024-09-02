package com.namoadigital.prj001.extensions.coroutines

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

fun <T> Flow<IResult<T>>.namoaCatch(
    local: String,
) = apply {
    this.catch { e ->
        ToolBox_Inf.registerException(local, IOException(e.message))
        emit(loading(false))
        emit(failed(e))
    }
}


fun <T> Flow<IResult<T>>.flowCatch(
    local: String,
) = this.catch { e ->
    ToolBox_Inf.registerException(local, IOException(e.message))
    emit(failed(e))
}
