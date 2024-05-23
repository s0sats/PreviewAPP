package com.namoadigital.prj001.extensions

import com.namoadigital.prj001.core.IResult

fun <DTO> IResult<DTO>.watchStatus(
    success: (data: DTO) -> Unit,
    error: (error: String) -> Unit = {},
    failed: (throwable: Throwable) -> Unit = {},
    loading: (message: String) -> Unit = {}
) {
    when (this) {
        is IResult.isSuccess -> success.invoke(this.response)
        is IResult.isError -> error.invoke(this.error)
        is IResult.isFailed -> failed.invoke(this.exceptionError)
        is IResult.isLoading -> loading.invoke(this.message)
    }
}