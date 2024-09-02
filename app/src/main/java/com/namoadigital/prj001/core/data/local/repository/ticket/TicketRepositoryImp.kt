package com.namoadigital.prj001.core.data.local.repository.ticket

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.core.sendToWebServiceReceiver
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.model.TkTicketCache
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download
import com.namoadigital.prj001.sql.trip.ticket.FsTripSqlLateTicket
import com.namoadigital.prj001.sql.trip.ticket.FsTripSqlNextTicket
import com.namoadigital.prj001.sql.trip.ticket.FsTripSqlPriorityTicket
import com.namoadigital.prj001.sql.trip.ticket.FsTripSqlTicketActions
import com.namoadigital.prj001.sql.trip.ticket.FsTripSqlTodayTicket
import com.namoadigital.prj001.util.ToolBox_Con
import javax.inject.Inject

class TicketRepositoryImp @Inject constructor(
    val app: Context,
    val dao: TK_TicketDao,
) : TicketRepository {
    override fun getTicketPriorityCntList(siteCode: Int): Int {
        val tickets = dao.query(
            FsTripSqlPriorityTicket(ToolBox_Con.getPreference_User_Code(app), siteCode).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTicketTodayCntList(siteCode: Int): Int {
        val tickets = dao.query(
            FsTripSqlTodayTicket(
                ToolBox_Con.getPreference_User_Code(app),
                siteCode,
                ToolBox.getDeviceGMT(false)
            ).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTicketLateCntList(siteCode: Int): Int {
        val tickets = dao.query(
            FsTripSqlLateTicket(
                ToolBox_Con.getPreference_User_Code(app),
                siteCode,
                ToolBox.getDeviceGMT(false)
            ).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTicketNextList(siteCode: Int): Int {
        val tickets = dao.query(
            FsTripSqlNextTicket(
                ToolBox_Con.getPreference_User_Code(app),
                siteCode
            ).toSqlQuery()
        )
        //
        return tickets.size
    }

    override fun getTicketActionList(
        siteCode: Int,
        isFocused: Int,
        multStepsLbl: String?,
        productCode: Int?,
        serialId: String?,
    ): List<HMAux> {
        return dao.query_HM(
            FsTripSqlTicketActions(
                ToolBox_Con.getPreference_Customer_Code(app),
                ToolBox_Con.getPreference_User_Code(app),
                siteCode,
                isFocused,
                multStepsLbl,
                serialId,
                productCode,
            ).toSqlQuery()
        )
    }

    override fun getTicket(
        prefix: Int,
        code: Int
    ): TK_Ticket? {
        return dao.getTicket(
            app.getCustomerCode(),
            prefix,
            code
        )
    }

    override fun getTicketKanban(prefix: Int, code: Int): TK_Ticket? {
        val ticket = getTicket(prefix, code)
        if (ticket?.kanban == 1) {
            return ticket
        }
        return null
    }

    override fun downloadTicket(
        prefix: Int,
        code: Int,
    ) {
        val processPk = "${app.getCustomerCode()}|$prefix|$code"

        app.sendToWebServiceReceiver<WBR_TK_Ticket_Download> {
            Bundle().apply {
                putString(TK_TicketDao.TICKET_PREFIX, processPk)
            }
        }
    }
}