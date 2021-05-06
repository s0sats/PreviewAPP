package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.TK_Ticket

class SqlAct083_001(
        private val customerCode: Int,
        private val tagOperCode: Int?,
        private val siteCode: Int?,
        private val productCode: Int?,
        private val serialId: String?,
        private val clientId: String?,
        private val contractId: String?,
        private val ticketId: String?
) : Specification {

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
                     and NOT EXISTS(SELECT 1
                                    FROM ${TK_TicketDao.TABLE} t
                                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = c.${TkTicketCacheDao.CUSTOMER_CODE}
                                          and t.${TK_TicketDao.TICKET_PREFIX} = c.${TkTicketCacheDao.TICKET_PREFIX}
                                          and t.${TK_TicketDao.TICKET_CODE} = c.${TkTicketCacheDao.TICKET_CODE}
                                          )
              """
    }
}