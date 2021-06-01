package com.namoadigital.prj001.sql

import android.content.Context
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

/**
 * BARRIONUEVO - 27-05-2021
 * Query responsavel por contar e retornar situação de update_required das acoes no menu
 * OS/IO
 */
class SqlAct005TagList002 (private val customerCode: Int) : Specification {

    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        val toString = sb.append(
                """

    select 
          sum(ticket.qty) qty, 
          max(ticket.update_required) update_required
    from (
            select count(1) qty, 
            max( max(tk.update_required), max(tk.update_required_product)) update_required
            from ${TK_TicketDao.TABLE} tk 
            where  tk.customer_code = $customerCode                      
                   and tk.ticket_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}' , '${ConstantBaseApp.SYS_STATUS_PROCESS}' , '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')                 
        --UNION TICKET CACHE
        UNION ALL 
            select count(1) qty, 
                   max(0) update_required 
             from ${TkTicketCacheDao.TABLE} tkc
             left join tk_ticket tk
                     on tk.customer_code = tkc.customer_code 
                    and tk.ticket_prefix = tkc.ticket_prefix 
                    and tk.ticket_code = tkc.ticket_code                    
            where tk.ticket_code is null
        --UNION FORM AP                              
        UNION ALL 
            select  count(1) qty,
                    max(geap.upload_required) update_required                  
            from ${GE_Custom_Form_ApDao.TABLE} geap                    
            where geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
              and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
        -- UNION Agendamento
        UNION ALL 
            select  count(1) qty,
                    max((case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                          then 1
                          else 0
                    end)) update_required
              from ${MD_Schedule_ExecDao.TABLE} mse
             where  mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
                and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_IN_PROCESSING}','${Constant.SYS_STATUS_WAITING_SYNC}')                   
        UNION ALL
            select count(1) qty, 
            max((case when gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end)) update_required
            from   ${GE_Custom_Form_LocalDao.TABLE} gcdl   
            where  gcdl.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = '$customerCode'
              and gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} in ('${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}', '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
              and gcdl.schedule_prefix is null
              and gcdl.schedule_code is null
              and gcdl.schedule_exec is null
              and gcdl.ticket_prefix is null
              and gcdl.ticket_code is null
              and gcdl.ticket_seq is null
              and gcdl.ticket_seq_tmp is null
              and gcdl.step_code is null               
        )  ticket 
                                                                """
        ).toString()
        return toString
    }
}