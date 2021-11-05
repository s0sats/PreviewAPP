package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.PROFILE_MENU_TICKET
import com.namoadigital.prj001.util.ConstantBaseApp.PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION

/**
 * BARRIONUEVO 04-11-2021
 * Query que seleciona serial com pendencia de estrutura.
 */
class MDProductSerialSql017(
    val customerCode: Long,
    val ticketPrefix: Int = -1,
    val ticketCode: Int = -1
): Specification {
    var ticketFilter = ""

    init {
        if(ticketPrefix > 0 && ticketCode > 0){
            ticketFilter = "and t.ticket_prefix = ${ticketPrefix} and t.ticket_code = ${ticketCode}"
        }
    }

    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        val toString = sb.append("""
                        SELECT
                             distinct c.customer_code, c.product_code, c.serial_code, ps.scn_item_check
                        FROM
                            ${TK_TicketDao.TABLE} t,
                            ${TK_Ticket_StepDao.TABLE} s,
                            ${TK_Ticket_CtrlDao.TABLE} c,
                            ${TK_Ticket_FormDao.TABLE}  f
                            left join ${MD_Product_SerialDao.TABLE}  ps
                                   on ps.customer_code = c.customer_code
                                  and ps.product_code = c.product_code
                                  and ps.serial_tmp = c.serial_code
                        WHERE
                            t.customer_code = ${customerCode}
                        and t.customer_code = s.customer_code
                        and t.ticket_prefix = s.ticket_prefix
                        and t.ticket_code = s.ticket_code
                        and t.current_step_order = s.step_order
                        and s.ticket_prefix = c.ticket_prefix
                        and s.ticket_code = c.ticket_code
                        and s.customer_code = c.customer_code
                        and s.step_code = c.step_code
                        and c.customer_code = f.customer_code
                        and c.ticket_prefix = f.ticket_prefix
                        and c.ticket_code = f.ticket_code
                        and c.ticket_seq_tmp = f.ticket_seq_tmp
                        ${ticketFilter}
                        and t.ticket_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')
                        and s.step_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')
                        and c.ctrl_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')
                        and c.ctrl_type = '${ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM}' 
                        and c.has_item_check = 1
                        and f.is_so = 1
                        and (s.user_focus = 1 
                            or
                                exists ( 
                                select 1 
                                from ev_profiles ep
                                where ep.customer_code = 1
                                and ep.menu_code = '${PROFILE_MENU_TICKET}'
                                and ep.parameter_code = '${PROFILE_MENU_TICKET_PARAM_CLAIM_SPECIAL_EXECUTION_PERMITION}'
                                )
                            )         
                        and ( 
                                not exists ( 
                                    select 1 
                                    from md_product_serial_tp_device mpstd
                                    where mpstd.product_code = c.product_code
                                    and mpstd.serial_code = c.serial_code
                                    )
                            )
                        group by  c.customer_code, c.product_code, c.serial_code
            """.trimIndent()
        ).toString()
        return toString
    }
}