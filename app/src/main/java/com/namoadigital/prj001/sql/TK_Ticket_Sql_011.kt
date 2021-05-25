package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao
import com.namoadigital.prj001.dao.TK_Ticket_StepDao
import com.namoadigital.prj001.database.Specification

/**
 * BARRIONUEVO 25-05-2021
 * Query que verifica se o ticket possui pendencia de envio em qualquer update_required da estrutura do ticket.
 *
 */
class TK_Ticket_Sql_011(
        val customer_code: Long,
        val ticket_prefix: Int,
        val ticket_code: Int
) : Specification {


    override fun toSqlQuery(): String? {
        val sb = StringBuilder()
        return sb
                .append(""" 
SELECT max( max(t.update_required),
            max(t.update_required_product),
            max(s.update_required),
            max(c.update_required)
          )  ${TK_TicketDao.UPDATE_REQUIRED} 
FROM
   ${TK_TicketDao.TABLE}  t,
   ${TK_Ticket_StepDao.TABLE}  s,
   ${TK_Ticket_CtrlDao.TABLE}  c
WHERE t.customer_code = s.customer_code
  and t.ticket_prefix = s.ticket_prefix
  and t.ticket_code = s.ticket_code
  and s.ticket_prefix = c.ticket_prefix
  and s.ticket_code = c.ticket_code
  and s.customer_code = c.customer_code
  and s.step_code = c.step_code
  and t.customer_code = '$customer_code'
  and t.ticket_prefix = '$ticket_prefix'
  and t.ticket_code = '$ticket_code'
group by t.ticket_prefix, t.ticket_code
""").toString()
    }
}
