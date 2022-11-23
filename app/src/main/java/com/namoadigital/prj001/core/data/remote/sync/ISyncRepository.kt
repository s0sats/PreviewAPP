package com.namoadigital.prj001.core.data.remote.sync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.Sync_ChecklistDao
import com.namoadigital.prj001.receiver.WBR_Sync
import com.namoadigital.prj001.sql.Sync_Checklist_Sql_002
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ISyncRepository constructor(
    private val context: Context,
    private val syncChecklistDao: Sync_ChecklistDao,
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

    override suspend fun checkSyncChecklistV2(productCode: Int): List<HMAux> {
        return syncChecklistDao.query_HM(
            Sync_Checklist_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                productCode.toLong()
            ).toSqlQuery()
        )
    }


    companion object {
        class SyncRepositoryFactory constructor(
            private val context: Context
        ) : NamoaFactory<SyncRepository>() {
            override fun build(): SyncRepository = ISyncRepository(
                context,
                Sync_ChecklistDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                )
            )

        }
    }
}