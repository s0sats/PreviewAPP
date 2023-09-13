package com.namoadigital.prj001.core.data.local.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.data.local.preferences.SiteInventoryPref
import com.namoadigital.prj001.model.SerialSiteInventory
import com.namoadigital.prj001.receiver.WBR_Serial_Site_Inv
import com.namoadigital.prj001.service.WsSerialSerialInventory
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class SerialSiteInventoryRepositoryImp constructor(
    private val context: Context,
    private val preference: SiteInventoryPref
) : SerialSiteInventoryRepository {
    override fun getSiteInventory(): SerialSiteInventory? {
        try {
            ToolBox_Inf.getContents(
                File(
                    Constant.SERIAL_SITE_INV_JSON_PATH,
                    WsSerialSerialInventory.FILE_NAME
                )
            ).let {
                return Gson().fromJson(it, SerialSiteInventory::class.java)
            }
        } catch (e: Exception) {
            ToolBox_Inf.registerException(javaClass.name, e)
            return null
        }
    }

    override fun execServiceSiteInventory(bundle: Bundle) {
        Intent(context, WBR_Serial_Site_Inv::class.java).apply {
            this.putExtras(bundle)
            context.sendBroadcast(this)
        }
    }

    override fun saveSiteInvetoryPreference(siteInventory: SiteInventory) {
        preference.write(siteInventory)
    }

    override fun getPreference(): SiteInventory {
        return preference.read()
    }

    override fun cleanPreference() {
        saveSiteInvetoryPreference(
            SiteInventory(-1, "", false)
        )
    }

    override fun deleteFile() {
        ToolBox_Inf.deleteFileListExceptionSafe(
            Constant.SERIAL_SITE_INV_JSON_PATH,
            WsSerialSerialInventory.FILE_NAME
        )
    }

    companion object {
        class RepositoryFactory(
            private val context: Context,
        ) : NamoaFactory<SerialSiteInventoryRepository>() {
            override fun build(): SerialSiteInventoryRepository {
                return SerialSiteInventoryRepositoryImp(
                    context,
                    SiteInventoryPref.instance(context)
                )
            }
        }
    }
}