package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_001(
        private val context: Context,
        private val customerCode: Int,
        private val tagOperCode: Int?,
        private val siteCode: Int?,
        private val productCode: Int?,
        private val serialId: String?,
        private val clientId: String?,
        private val contractId: String?,
        private val ticketId: String?,
        private val calendarDate: String?
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private val dateFilter: String =
            when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION)){
                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
                else -> ""
            }

    override fun toSqlQuery(): String {
        return  """ SELECT
                     c.*
                    FROM
                     ${TkTicketCacheDao.TABLE} c 
                    WHERE
                     c.${TkTicketCacheDao.CUSTOMER_CODE} = $customerCode
                     and ($tagOperCode is null or c.${TkTicketCacheDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                     and ($siteCode is null or c.${TkTicketCacheDao.OPEN_SITE_CODE}  = $siteCode)
                     and ($productCode is null or c.${TkTicketCacheDao.OPEN_PRODUCT_CODE} = $productCode )
                     and ($serialId is null or c.${TkTicketCacheDao.OPEN_SERIAL_ID} = $serialId )
                     and ($clientId is null or c.${TkTicketCacheDao.CLIENT_ID} = $clientId)
                     and ($contractId is null or c.${TkTicketCacheDao.CONTRACT_ID} = $contractId)
                     and ($ticketId is null or c.${TkTicketCacheDao.TICKET_ID} = $ticketId)
                     and ($calendarDate is null or strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') = $calendarDate )
                     $dateFilter
                     and NOT EXISTS(SELECT 1
                                    FROM ${TK_TicketDao.TABLE} t
                                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = c.${TkTicketCacheDao.CUSTOMER_CODE}
                                          and t.${TK_TicketDao.TICKET_PREFIX} = c.${TkTicketCacheDao.TICKET_PREFIX}
                                          and t.${TK_TicketDao.TICKET_CODE} = c.${TkTicketCacheDao.TICKET_CODE}
                                          )
              """
    }
}