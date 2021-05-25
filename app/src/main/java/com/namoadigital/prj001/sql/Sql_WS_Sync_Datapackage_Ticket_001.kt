package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.TK_Ticket
import com.namoadigital.prj001.util.ConstantBaseApp

class Sql_WS_Sync_Datapackage_Ticket_001(
         private val customerCode: Long
        ):Specification {
    override fun toSqlQuery(): String {
        var s = """ SELECT
                     t.${TK_TicketDao.CUSTOMER_CODE},
                     t.${TK_TicketDao.TICKET_PREFIX},
                     t.${TK_TicketDao.TICKET_CODE},
                     t.${TK_TicketDao.SCN}
                    FROM
                     ${TK_TicketDao.TABLE} t
                    WHERE                    
                     t.${TK_TicketDao.CUSTOMER_CODE} = $customerCode
                     AND t.${TK_TicketDao.TICKET_STATUS} IN ('${ConstantBaseApp.SYS_STATUS_PENDING}',
                                                                  '${ConstantBaseApp.SYS_STATUS_PROCESS}',
                                                                  '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                                                                )                                                
                """
        return s.trimIndent()
    }
}