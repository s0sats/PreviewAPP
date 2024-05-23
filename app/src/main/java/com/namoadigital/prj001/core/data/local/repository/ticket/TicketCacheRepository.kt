package com.namoadigital.prj001.core.data.local.repository.ticket

import com.namoadigital.prj001.model.TkTicketCache

interface TicketCacheRepository {
    fun getPriorityCntList(siteCode: Int): Int
    fun getTodayCntList(siteCode: Int): Int
    fun getLateCntList(siteCode: Int): Int
    fun getNextList(siteCode: Int): Int
    fun getTicketActionList(siteCode: Int, isFocused: Int): List<TkTicketCache>
    fun getTicketKanban(prefix: Int, code: Int): TkTicketCache?
}