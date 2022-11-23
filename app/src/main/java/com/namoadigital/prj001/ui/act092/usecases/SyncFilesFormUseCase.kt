package com.namoadigital.prj001.ui.act092.usecases

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.data.remote.sync.SyncRepository
import com.namoadigital.prj001.model.DataPackage
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp

class SyncFilesFormUseCase constructor(
    private val syncRepository: SyncRepository
) {

    operator fun invoke(hmAux: HMAux, productCode: Long) {

        syncRepository.sync(
            arrayListOf(DataPackage.DATA_PACKAGE_CHECKLIST),
            hmAux,
            Bundle().apply {
                putLong(Constant.GS_PRODUCT_CODE, productCode)
                putLong(Constant.GC_STATUS_JUMP, 1)
                putLong(Constant.GC_STATUS, 1)
                putInt(Constant.WS_CONNECTION_TIMEOUT, ConstantBaseApp.TIMEOUT_FOR_SYNC_FORM)

            }
        )

    }

}
