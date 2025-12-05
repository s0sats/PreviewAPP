package com.namoadigital.prj001.sql.ticket

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.dao.TK_Ticket_StepDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlWSTKTicketBigFile001(
    val customer_code: Long,
    val siteCode: String
) : Specification {
    override fun toSqlQuery(): String {
        var s = """
            SELECT 
          DISTINCT t.customer_code ||'|'|| t.ticket_prefix ||'|'|| t.ticket_code ||'|'|| t.scn "+TICKET_PK+",
                   t.ticket_prefix,
                   t.ticket_code,
                   t.scn
              FROM ${TK_TicketDao.TABLE} t,
                   ${TK_Ticket_StepDao.TABLE} s,
                   ${TK_Ticket_CtrlDao.TABLE} c
                          
             WHERE t.${TK_TicketDao.CUSTOMER_CODE} = $customer_code    
               and t.${TK_TicketDao.TICKET_PREFIX} = s.${TK_Ticket_StepDao.TICKET_PREFIX}
               and t.${TK_TicketDao.TICKET_CODE} = s.${TK_Ticket_StepDao.TICKET_CODE}
               and s.${TK_Ticket_StepDao.TICKET_PREFIX} = c.${TK_Ticket_CtrlDao.TICKET_PREFIX}
               and s.${TK_Ticket_StepDao.TICKET_CODE} = c.${TK_Ticket_CtrlDao.TICKET_CODE}
               and t.${TK_TicketDao.SYNC_BIG_FILE} = 1
               and t.update_required_product = 0
               and t.update_required_status = 0
               and t.update_required = 0
               and s.update_required = 0
               and c.update_required = 0
               and NOT EXISTS(SELECT 1
                                FROM ge_custom_form_datas d
                               WHERE d.customer_code = c.customer_code
                                 AND d.ticket_prefix = c.ticket_prefix
                                 AND d.ticket_code = c.ticket_code
                                 AND d.custom_form_status = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                                 AND d.location_pendency = 1
                             )
             ORDER BY (t.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode) DESC
            
        """
            .trimMargin()

        return s
    }
}