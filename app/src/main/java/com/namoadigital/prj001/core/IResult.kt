package com.namoadigital.prj001.core

sealed class IResult<out DTO> {

    data class isSuccess<out DTO>(val response: DTO) : IResult<DTO>()

    data class isFailed(val exceptionError: Throwable) : IResult<Nothing>() {
        val message = exceptionError.message
    }

    data class isLoading(val isLoading: Boolean, val message: String) : IResult<Nothing>()


    companion object {

        fun <OUT> success(data: OUT) = isSuccess(data)

        fun failed(exceptionError: Throwable) = isFailed(exceptionError)

        fun loading(isLoading: Boolean, message: String = "Waiting...") =
            isLoading(isLoading, message)

        suspend fun <DTO> IResult<DTO>.isSuccess(block: suspend (DTO) -> Unit) = apply {
            if (this is isSuccess) {
                block(this.response)
            }
        }

        suspend fun <DTO> IResult<DTO>.isFailed(block: suspend (exception: Throwable) -> Unit) =
            apply {
                if (this is isFailed) {
                    block(this.exceptionError)
                }
            }

        suspend fun <DTO> IResult<DTO>.isLoading(block: suspend (isLoading: Boolean, message: String) -> Unit) =
            apply {
                if (this is isLoading) {
                    block(this.isLoading, this.message)
                }
            }

    }
}