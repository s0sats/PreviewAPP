package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.extensions.coroutines.flowCatch
import kotlinx.coroutines.flow.Flow

fun <DTO> IResult<DTO>.watchStatus(
    success: (data: DTO) -> Unit,
    error: (error: String?, throwable: Throwable?) -> Unit = { _, _ -> },
    failed: (throwable: Throwable) -> Unit = {},
    loading: (isLoading: Boolean, translateKey: String) -> Unit = { _, _ -> }
) {
    when (this) {
        is IResult.isSuccess -> success.invoke(this.response)
        is IResult.isError -> error.invoke(this.message, this.exceptionError)
        is IResult.isFailed -> failed.invoke(this.exceptionError)
        is IResult.isLoading -> loading.invoke(this.isLoading, this.message)
    }
}


suspend fun <DTO> IResult<DTO>.results(
    success: suspend (data: DTO) -> Unit,
    error: suspend (error: String?, exceptionError: Throwable?) -> Unit = { _, _ -> },
    failed: suspend (throwable: Throwable) -> Unit = {},
    loading: suspend (isLoading: Boolean, message: String) -> Unit = { _, _ -> }
) {
    when (this) {
        is IResult.isSuccess -> success.invoke(this.response)
        is IResult.isError -> error.invoke(this.message, this.exceptionError)
        is IResult.isFailed -> failed.invoke(this.exceptionError)
        is IResult.isLoading -> loading.invoke(this.isLoading, this.message)
    }
}

suspend fun <DTO> Flow<IResult<DTO>>.suspendResults(
    success: suspend (data: DTO) -> Unit,
    error: suspend (error: String?, exceptionError: Throwable?) -> Unit = { _, _ -> },
    failed: suspend (throwable: Throwable) -> Unit = {},
    loading: suspend (isLoading: Boolean, message: String) -> Unit = { _, _ -> }
) {
    this.flowCatch(this::class.java.name)
        .collect {
            it.results(
                success = success,
                error = error,
                failed = { throwable ->
                    loading(false, throwable.localizedMessage ?: "")
                    return@results failed(throwable)
                },
                loading = loading
            )
        }
}

suspend fun <DTO> Flow<IResult<DTO>>.results(
    success: (data: DTO) -> Unit,
    error: (error: String?, exceptionError: Throwable?) -> Unit = { _, _ -> },
    failed: (throwable: Throwable) -> Unit = {},
    loading: (isLoading: Boolean, message: String) -> Unit = { _, _ -> }
) = this.collect { it.watchStatus(success, error, failed, loading) }