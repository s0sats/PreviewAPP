package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.GE_Custom_Form
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_002(
        private val context: Context,
        private val customerCode: Int,
        private val tagOperCode: Int?,
        private val siteCode: Int?,
        private val productCode: Int?,
        private val serialId: String?,
        private val clientId: String?,
        private val contractId: String?,
        private val ticketId: String?,
        private val calendarDate: String?,
        private val userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private val dateFilter: String =
            when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION)){
                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and  (strftime('%Y-%m-%d',t.${TK_TicketDao.FORECAST_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and  (strftime('%Y-%m-%d',t.${TK_TicketDao.FORECAST_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
                else -> ""
            }

    override fun toSqlQuery(): String {
        return  """SELECT
                     t.*
                    FROM
                     ${TK_TicketDao.TABLE} t
                    WHERE                     
                         t.${TK_TicketDao.CUSTOMER_CODE} = $customerCode
                          and ($calendarDate is null or strftime('%Y-%m-%d', t.${TK_TicketDao.FORECAST_DATE}, '$deviceGMT') = $calendarDate )
                          and ( EXISTS (SELECT 1
                                        FROM ${GE_Custom_Form_LocalDao.TABLE} l
                                        WHERE l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                             and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                             and l.${GE_Custom_Form_LocalDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                             and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                                        )        
                              OR (  
                                     t.${TK_TicketDao.USER_FOCUS} = $userFocus
                                     and ($tagOperCode is null or t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                                     and ($siteCode is null or t.${TK_TicketDao.OPEN_SITE_CODE}  = $siteCode)
                                     and ($productCode is null or t.${TK_TicketDao.OPEN_PRODUCT_CODE}  = $productCode )
                                     and ($serialId is null or t.${TK_TicketDao.OPEN_SERIAL_ID}  = $serialId )
                                     and ($clientId is null or t.${TK_TicketDao.CLIENT_ID}  = $clientId)
                                     and ($contractId is null or t.${TK_TicketDao.CONTRACT_ID}  = $contractId)
                                     and ($ticketId is null or t.${TK_TicketDao.TICKET_ID}  = $ticketId)                         
                                     $dateFilter
                                    )
                          )  
              """
    }
}