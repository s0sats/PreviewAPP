package com.namoadigital.prj001.core

sealed class IResult<out DTO> {

    data class isSuccess<out DTO>(val response: DTO) : IResult<DTO>()

    data class isError(val message: String? = null, val exceptionError: Throwable? = null) :
        IResult<Nothing>()

    data class isFailed(val exceptionError: Throwable) : IResult<Nothing>() {
        val message = exceptionError.message
    }

    data class isLoading(val isLoading: Boolean, val message: String) : IResult<Nothing>()


    companion object {

        fun <OUT> success(data: OUT) = isSuccess(data)

        fun failed(exceptionError: Throwable) = isFailed(exceptionError)
        fun error(message: String? = null, exceptionError: Throwable? = null) = isError(message, exceptionError)

        fun loading(isLoading: Boolean = true, message: String = "Waiting...") =
            isLoading(isLoading, message)

        suspend fun <DTO> IResult<DTO>.isSuccess(block: suspend (DTO) -> Unit) = apply {
            if (this is isSuccess) {
                block(this.response)
            }
        }

        fun <DTO> IResult<DTO>.success(block: (DTO) -> Unit) = apply {
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

        fun <DTO> IResult<DTO>.failed(block: (exception: Throwable) -> Unit) =
            apply {
                if (this is isFailed) {
                    block(this.exceptionError)
                }
            }

        suspend fun <DTO> IResult<DTO>.isError(block: suspend (message: String?, exceptionError: Throwable?) -> Unit) =
            apply {
                if (this is isError) {
                    block(this.message, this.exceptionError)
                }
            }

        fun <DTO> IResult<DTO>.error(block: (error: String?, exceptionError: Throwable?) -> Unit) =
            apply {
                if (this is isError) {
                    block(this.message, this.exceptionError)
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