package com.namoadigital.prj001.ui.act092.usecases

import android.os.Bundle
import com.namoadigital.prj001.service.WS_UnfocusAndHistoric
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository

class UnfocusHistoricalActionUseCases(
    private val repository: ActionSerialRepository
) {
    operator fun invoke(productCode: Int, serialCode: Long) {
        repository.unfocusAndHistorical(Bundle().apply {
            putInt(WS_UnfocusAndHistoric.PRODUCT_CODE, productCode) //Valida Update require
            putLong(WS_UnfocusAndHistoric.SERIAL_CODE, serialCode)
        })
    }
}
