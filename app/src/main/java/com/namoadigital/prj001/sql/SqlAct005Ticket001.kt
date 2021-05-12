package com.namoadigital.prj001.sql

import android.content.Context
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.dao.TK_TicketDao.FORECAST_DATE
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION

class SqlAct005Ticket001(private val context: Context,
                         private val customerCode: Int,
                         deviceGMT: String,
                         siteCode: Int,
                         periodFilter: String,
                         focusFilter: String
) : Specification {
    var ticketFilter = ""
    var formApFilter = ""
    var scheduleFilter = ""
    init{
        ticketFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        //
        formApFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n (strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n (strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        //
        scheduleFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }

        if (siteCode > 0) {
            ticketFilter += "\n and tk.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode "
            scheduleFilter += "\n and mse.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode "
        }

        if (PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION.equals(focusFilter)) {
            ticketFilter += "\n and tk.${TK_TicketDao.USER_FOCUS} = 1 "
        }
    }

    override fun toSqlQuery(): String {
        val sb = StringBuilder()
        return sb.append(
"""select ticket.tag_operational_code tag_operational_code, 
          ticket.tag_operational_desc tag_operational_desc, 
          sum(qty) qty, 
          max(ticket.update_required) update_required, 
          max(ticket.sync_required) sync_required,
          max(ticket.in_processing) in_processing
    from (
        select tk.tag_operational_code, 
               tk.tag_operational_desc , 
               count(tk.tag_operational_code) qty, 
               max( max(tk.update_required), max(tk.update_required_product)) update_required, 
               max(tk.sync_required) sync_required,
               0 in_processing 
          from ${TK_TicketDao.TABLE} tk 
            JOIN tk_ticket_step s ON 
                   tk.customer_code = s.customer_code 
                   AND tk.ticket_code = s.ticket_code 
                   AND tk.ticket_prefix = s.ticket_prefix
                 where   tk.customer_code = $customerCode 
                         and s.step_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}',  
                         '${ConstantBaseApp.SYS_STATUS_PROCESS}' ) 
                         and tk.ticket_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}' , '${ConstantBaseApp.SYS_STATUS_PROCESS}' , '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')  
                         and tk.current_step_order = s.step_order
                         and tk.ticket_code = s.ticket_code 
                         AND tk.ticket_prefix = s.ticket_prefix
                        $ticketFilter
        union 
            select tkc.tag_operational_code, 
                   tkc.tag_operational_desc, 
                   count(tkc.tag_operational_code) qty, 
                   max(0) update_required, 
                   max(0) sync_required, 
                   0 in_processing 
             from ${TkTicketCacheDao.TABLE} tkc
             left join tk_ticket tk
                     on tk.customer_code = tkc.customer_code 
                    and tk.ticket_prefix = tkc.ticket_prefix 
                    and tk.ticket_code = tkc.ticket_code
                    and tk.ticket_id = tkc.ticket_id
                where tk.ticket_code is null
        union 
            select  mdt.${MdTagDao.TAG_CODE}, 
                    mdt.${MdTagDao.TAG_DESC}, 
                    count(geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}) qty,
                     0 updated_required,
                     0 sync_required,
                     0 in_processing
              from ${MdTagDao.TABLE} mdt
             inner join ${GE_Custom_Form_ApDao.TABLE} geap
                    on  mdt.${MdTagDao.TAG_CODE} = geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}
             where geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
               and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
            group by mdt.${MdTagDao.TAG_CODE} 
        union 
            select mdt.${MdTagDao.TAG_CODE}, 
            mdt.${MdTagDao.TAG_DESC}, 
            count(mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}) qty,
            (case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end) updated_required,
             0 sync_required,
             0 in_processing
              from ${MdTagDao.TABLE} mdt
            inner join ${MD_Schedule_ExecDao.TABLE} mse
                    on  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
             where 
            mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
            and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_WAITING_SYNC}')
            $scheduleFilter
            group by mdt.${MdTagDao.TAG_CODE} 
        union
            select gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_CODE}, 
                   gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_DESC}, 
                   count(gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_CODE}), 
            (case when gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end) updated_required,
            0 sync_required,
            (case when gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} =  '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                  then 1
                  else 0
            end) in_processing
            from   ${GE_Custom_Form_LocalDao.TABLE} gcdl   
            where  gcdl.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = '$customerCode'
            and gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} in ('${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}', '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')  
            )  ticket 
   where  ticket.tag_operational_code is not null
   group by ticket.tag_operational_code, ticket.tag_operational_desc;"""


//                "select mdt.tag_desc, count(mdt.tag_code) \n" +
//                "  from " + MdTagDao.TABLE +" mdt\n" +
//                " inner join " + GE_Custom_Form_LocalDao.TABLE +" geform \n" +
//                "    on  mdt.tag_code = geform.tag_operational_code \n" +
//                " where " +
//                formFilter +
//                " group by mdt.tag_code"
//              + "union \n" +
//                "select mdt.tag_desc, count(mdt.tag_code)\n" +
//                "  from md_tag mdt\n" +
//                " inner join "+ TK_TicketDao.TABLE + " tk\n" +
//                "    on  mdt.tag_code = tk.tag_operational_code\n" +
//                " where tk.ticket_status = " + ConstantBaseApp.SYS_STATUS_PROCESS + "\n"+
//                ticketFilter +
//                " group by mdt.tag_code\n"
        ).toString()
    }
}