package com.namoadigital.prj001.core.data.remote.sync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.receiver.WBR_Sync
import com.namoadigital.prj001.ui.base.FactoryRepository
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ISyncRepository constructor(
    private val context: Context
) : SyncRepository {

    override fun sync(data_package: ArrayList<String>, hmAux: HMAux, bundle: Bundle) {
        Intent(context, WBR_Sync::class.java).also {
            it.putExtras(bundle.apply {
                putString(
                    Constant.GS_SESSION_APP,
                    ToolBox_Con.getPreference_Session_App(context)
                )
                //
                putStringArrayList(Constant.GS_DATA_PACKAGE, data_package)
            })

            context.sendBroadcast(it)
            ToolBox_Inf.sendBCStatus(context, "STATUS", hmAux["msg_start_sync"], "", "0")
        }
    }


    companion object {
        class SyncRepositoryFactory constructor(
            private val context: Context
        ) : FactoryRepository<SyncRepository>() {
            override fun build(): SyncRepository = ISyncRepository(context)

        }
    }
}