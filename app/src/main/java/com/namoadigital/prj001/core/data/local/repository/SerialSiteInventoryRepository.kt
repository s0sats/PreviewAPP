package com.namoadigital.prj001.core.data.local.repository

import android.os.Bundle
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.model.SerialSiteInventory

interface SerialSiteInventoryRepository {

    fun getSiteInventory(): List<SerialSiteInventory>
    fun execServiceSiteInventory(bundle: Bundle)
    fun saveSerialSiteInvetoryPreference(siteInventory: SiteInventory)

    fun editPreference(map: HashMap<String, Any>)

    fun getPreference(): SiteInventory

    fun cleanPreference()

    fun deleteFile()
}