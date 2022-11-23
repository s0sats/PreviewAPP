package com.namoadigital.prj001.core.data.remote.sync

import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux

interface SyncRepository {

    fun sync(data_package: ArrayList<String>, hmAux: HMAux, bundle: Bundle)

    suspend fun checkSyncChecklistV2(productCode: Int): List<HMAux>
}