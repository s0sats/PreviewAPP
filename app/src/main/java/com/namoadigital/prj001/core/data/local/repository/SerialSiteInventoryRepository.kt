package com.namoadigital.prj001.core.data.local.repository

import android.os.Bundle
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.model.SerialSiteInventory

interface SerialSiteInventoryRepository {

    fun getSiteInventory(): SerialSiteInventory?
    fun execServiceSiteInventory(bundle: Bundle)
    fun saveSiteInvetoryPreference(siteInventory: SiteInventory)

    fun getPreference(): SiteInventory

    fun cleanPreference()

    fun deleteFile()
}