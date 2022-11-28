package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class CheckFileExistsUseCase constructor(
    private val repository: ActionSerialRepository
) {

    suspend operator fun invoke(productCode: Int, serialCode: Long): Boolean {
        return repository.getUnfocusAndHistorical(
            productCode,
            serialCode
        ).isEmpty()
    }

}
