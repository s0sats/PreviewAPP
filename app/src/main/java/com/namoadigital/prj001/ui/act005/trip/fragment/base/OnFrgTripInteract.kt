package com.namoadigital.prj001.ui.act005.trip.fragment.base

import android.os.Bundle
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.model.MyActionFilterParam

interface OnFrgTripInteract {
    fun addDestination()
    fun callTripActions(myActionFilterParam: MyActionFilterParam, siteInventory: SiteInventory)
    fun callSerialSearch()

    fun updateFooter()
    fun callActivityTicket(bundle: Bundle)
    fun callActivityOs(bundle: Bundle)
    fun isEnabledGps() : Boolean
    fun unregisterGpsReceiver()
}