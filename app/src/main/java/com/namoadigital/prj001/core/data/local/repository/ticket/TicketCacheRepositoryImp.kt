package com.namoadigital.prj001.core.data.local.repository.ticket

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.sql.trip.ticket_cache.FsTripSqlLateTicketCache
import com.namoadigital.prj001.sql.trip.ticket_cache.FsTripSqlNextTicketCache
import com.namoadigital.prj001.sql.trip.ticket_cache.FsTripSqlPriorityTicketCache
import com.namoadigital.prj001.sql.trip.ticket_cache.FsTripSqlTicketCacheActions
import com.namoadigital.prj001.sql.trip.ticket_cache.FsTripSqlTodayTicketCache
import com.namoadigital.prj001.util.ToolBox_Con
import javax.inject.Inject

class TicketCacheRepositoryImp @Inject constructor(
    val app: Context,
    val dao: TkTicketCacheDao
): TicketCacheRepository {
    override fun getPriorityCntList(siteCode:Int): Int {
        val tickets = dao.query(
            FsTripSqlPriorityTicketCache(ToolBox_Con.getPreference_User_Code(app), siteCode).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTodayCntList(siteCode:Int): Int {
        val tickets = dao.query(
            FsTripSqlTodayTicketCache(ToolBox_Con.getPreference_User_Code(app), siteCode, ToolBox.getDeviceGMT(false)).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getLateCntList(siteCode:Int): Int {
        val tickets = dao.query(
            FsTripSqlLateTicketCache(ToolBox_Con.getPreference_User_Code(app), siteCode, ToolBox.getDeviceGMT(false)).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getNextList(siteCode:Int): Int {
        val tickets = dao.query(
            FsTripSqlNextTicketCache(ToolBox_Con.getPreference_User_Code(app), siteCode).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTicketActionList(siteCode: Int, isFocused: Int): List<TkTicketCache> {
        return dao.query(
            FsTripSqlTicketCacheActions(
                ToolBox_Con.getPreference_User_Code(app),
                siteCode,
                isFocused
            ).toSqlQuery()
        )
    }

    override fun getTicketKanban(prefix: Int, code: Int): TkTicketCache? {
        return dao.getTicketKanban(prefix, code)
    }
}