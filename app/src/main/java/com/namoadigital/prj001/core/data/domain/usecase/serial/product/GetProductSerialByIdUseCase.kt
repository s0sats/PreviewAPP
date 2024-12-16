package com.namoadigital.prj001.core.data.domain.usecase.serial.product

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.data.local.repository.serial.ProductSerialRepository
import com.namoadigital.prj001.model.MD_Product_Serial
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductSerialByIdUseCase @Inject constructor(
    private val repository: ProductSerialRepository
) : UseCases<GetProductSerialByIdUseCase.Param, MD_Product_Serial?> {

    data class Param(
        val productCode: Long,
        val serialId: String
    )

    override suspend fun invoke(input: Param): Flow<IResult<MD_Product_Serial?>> {
        return repository.getProductSerialById(
            productCode = input.productCode,
            serialId = input.serialId
        )
    }

}