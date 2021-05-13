package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_001(
        private val context: Context,
        private val originFlow: String,
        private val customerCode: Int,
        private var tagOperCode: Int?,
        private var siteCode: String?,
        private var productCode: Int?,
        private var serialId: String?,
        private var clientId: String?,
        private var contractId: String?,
        private var ticketId: String?,
        private var calendarDate: String?,
        private val userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
    private var statusFilter: String =""

    init {
        setFiltersByOriginAndFocus()
    }

    private fun setFiltersByOriginAndFocus() {
        when(originFlow){
            ConstantBaseApp.ACT005 -> setHomeFilterConfg()
        }
    }

    private fun setHomeFilterConfg() {
        periodDateFilter = when (ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        productCode = null
        serialId = null
        clientId = null
        contractId = null
        calendarDate = null
        statusFilter = when(userFocus){
                        1 -> """    and c.${TkTicketCacheDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}') """
                        else -> """    and c.${TkTicketCacheDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     c.*
                    FROM
                     ${TkTicketCacheDao.TABLE} c 
                    WHERE
                     c.${TkTicketCacheDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter
                     and ($userFocus is null or c.${TkTicketCacheDao.USER_FOCUS} = $userFocus)                     
                     and ($tagOperCode is null or c.${TkTicketCacheDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                     and ($siteCode is null or c.${TkTicketCacheDao.OPEN_SITE_CODE}  = $siteCode)
                     and ($productCode is null or c.${TkTicketCacheDao.OPEN_PRODUCT_CODE} = $productCode )
                     and ($serialId is null or c.${TkTicketCacheDao.OPEN_SERIAL_ID} = $serialId )
                     and ($clientId is null or c.${TkTicketCacheDao.CLIENT_ID} = $clientId)
                     and ($contractId is null or c.${TkTicketCacheDao.CONTRACT_ID} = $contractId)
                     and ($ticketId is null or c.${TkTicketCacheDao.TICKET_ID} = $ticketId)
                     and ($calendarDate is null or strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') = $calendarDate )
                     $periodDateFilter
                     and NOT EXISTS(SELECT 1
                                    FROM ${TK_TicketDao.TABLE} t
                                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = c.${TkTicketCacheDao.CUSTOMER_CODE}
                                          and t.${TK_TicketDao.TICKET_PREFIX} = c.${TkTicketCacheDao.TICKET_PREFIX}
                                          and t.${TK_TicketDao.TICKET_CODE} = c.${TkTicketCacheDao.TICKET_CODE}
                                          )
              """
        return s
    }
}