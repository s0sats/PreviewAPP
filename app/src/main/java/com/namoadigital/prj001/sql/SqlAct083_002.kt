package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification

class SqlAct083_002(
        private val customerCode: Int,
        private val tagOperCode: Int?,
        private val siteCode: Int?,
        private val productCode: Int?,
        private val serialId: String?,
        private val clientId: String?,
        private val contractId: String?,
        private val ticketId: String?,
        private val userFocus: Int,
) : Specification {

    override fun toSqlQuery(): String {
        return  """SELECT
                     t.*
                    FROM
                     ${TK_TicketDao.TABLE} t
                    WHERE                     
                         t.customer_code = $customerCode
                         and t.user_focus = $userFocus
                         and ($tagOperCode is null or t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                         and ($siteCode is null or t.${TK_TicketDao.OPEN_SITE_CODE}  = $siteCode)
                         and ($productCode is null or t.${TK_TicketDao.OPEN_PRODUCT_CODE}  = $productCode )
                         and ($serialId is null or t.${TK_TicketDao.OPEN_SERIAL_ID}  = $serialId )
                         and ($clientId is null or t.${TK_TicketDao.CLIENT_ID}  = $clientId)
                         and ($contractId is null or t.${TK_TicketDao.CONTRACT_ID}  = $contractId)
                         and ($ticketId is null or t.${TK_TicketDao.TICKET_ID}  = $ticketId)                         
                         --and (strftime('s%',t.${TK_TicketDao.FORECAST_DATE} )*1000) < strftime('s%','now') * 1000
              """
    }
}