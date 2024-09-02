package com.namoadigital.prj001.ui.act005.trip.fragment.base

import com.namoadigital.prj001.model.MainTagMenu

interface OnFrgMainHomeInteract {
    fun onSelectMenuTagItem(item: MainTagMenu)
    //
    fun onSelectFABAssetLocal()
    //
    fun onSelectHome()
    //
    fun onSelectCalendar()
    //
    fun onSelectTrip()
    //
    fun onSelectSearch()
    //
    fun onSelectMessenger()
    //
    fun getTagList(periodFilter: String, sitesFilter: String, focusFilter: String): MutableList<MainTagMenu>
    fun getDatetimeWarning(): String
    fun getChatBadgeQty(): Int
    fun showHomeBtn(): Boolean
    fun callTripWS(wsProcess: String, ttl: String, msg: String)
    fun hideProgressWs()
    fun checkGPSPermission()
    fun onSelectExtract()

    fun isEnabledGps() : Boolean
    fun invalidateMenuOptions()
    fun sendTripUpdateRequired()

}