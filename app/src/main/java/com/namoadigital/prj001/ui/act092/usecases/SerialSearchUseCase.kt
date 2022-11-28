package com.namoadigital.prj001.ui.act092.usecases

import android.os.Bundle
import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.Constant

class SerialSearchUseCase constructor(
    private val repository: ActionSerialRepository
) {

    operator fun invoke(
        productCode: String,
        productId: String,
        serialId: String,
        searchExact: Int
    ) {
        Bundle().apply {
            putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, productCode)
            putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, productId)
            putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serialId)
            putInt(Constant.WS_SERIAL_SEARCH_EXACT, searchExact)
        }.let {
            repository.searchSerialWS(it)
        }

    }

}
