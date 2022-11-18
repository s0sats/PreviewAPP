package com.namoadigital.prj001.ui.act092.usecases

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.data.remote.sync.SyncRepository
import com.namoadigital.prj001.model.DataPackage
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class SyncFilesUseCase(
    private val context: Context,
    private val syncRepository: SyncRepository
) {
    operator fun invoke(hmAux: HMAux) {
        ArrayList<String>().also {

            it.add(DataPackage.DATA_PACKAGE_MAIN)
            it.add(DataPackage.DATA_PACKAGE_CHECKLIST)
            it.add(DataPackage.DATA_PACKAGE_CHECKLIST)
            if (ToolBox_Inf.profileExists(
                    context,
                    Constant.PROFILE_PRJ001_SCHEDULE_CHECKLIST,
                    null
                )
            ) {
                it.add(DataPackage.DATA_PACKAGE_SCHEDULE)
            }

            if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
                it.add(DataPackage.DATA_PACKAGE_SO)
            }
        }.let { list ->

            syncRepository.sync(list, hmAux, Bundle().apply {
                putInt(Constant.GC_STATUS_JUMP, 0) //Valida Update require

                putInt(Constant.GC_STATUS, 1)

                putInt(Constant.WS_CONNECTION_TIMEOUT, ConstantBaseApp.TIMEOUT_FOR_SYNC_FULL)

            })
        }
    }
}