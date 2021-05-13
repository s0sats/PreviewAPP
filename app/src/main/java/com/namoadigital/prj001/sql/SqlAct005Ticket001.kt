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
    var scheduleNextFilter = ""
    var formFilter = ""
    init{
        ticketFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        //
        formApFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> """ and ( (case when gcdl.${GE_Custom_Form_ApDao.AP_WHEN} is null or gcdl.${GE_Custom_Form_ApDao.AP_WHEN} = "" 
                      then strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT'))
                      else strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.CREATE_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT'))
                 end)"""
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION ->""" and ( (case when gcdl.${GE_Custom_Form_ApDao.AP_WHEN} is null or gcdl.${GE_Custom_Form_ApDao.AP_WHEN} = "" 
                      then strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT'))
                      else strftime('%Y-%m-%d',geap.${GE_Custom_Form_ApDao.CREATE_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT', '+7 days'))
                 end)"""
            else -> ""
        }
        //
        scheduleFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        scheduleNextFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') > strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') > strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> "\n and (strftime('%Y-%m-%d',mse.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') >= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
        }

        if (siteCode > 0) {
            ticketFilter += "\n and tk.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode "
            scheduleFilter += "\n and mse.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode "
            scheduleNextFilter += "\n and mse.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode "
            formFilter += "\n and gcdl.${GE_Custom_Form_LocalDao.SITE_CODE} = $siteCode "
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
               max(0) in_processing 
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
                   max(0) in_processing 
             from ${TkTicketCacheDao.TABLE} tkc
             left join tk_ticket tk
                     on tk.customer_code = tkc.customer_code 
                    and tk.ticket_prefix = tkc.ticket_prefix 
                    and tk.ticket_code = tkc.ticket_code
                    and tk.ticket_id = tkc.ticket_id
                where tk.ticket_code is null
        union 
            select  mdt.${MdTagDao.TAG_CODE} tag_operational_code, 
                    mdt.${MdTagDao.TAG_DESC} tag_operational_desc, 
                    count(geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}) qty,
                     max(0) update_required,
                     max(0) sync_required,
                     max(0) in_processing
              from ${MdTagDao.TABLE} mdt
             inner join ${GE_Custom_Form_ApDao.TABLE} geap
                    on  mdt.${MdTagDao.TAG_CODE} = geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}
             where geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
               and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
            group by mdt.${MdTagDao.TAG_CODE} 
        union 
            select mdt.${MdTagDao.TAG_CODE} tag_operational_code, 
            mdt.${MdTagDao.TAG_DESC} tag_operational_desc, 
            count(mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}) qty,
            max((case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end)) update_required,
             max(0) sync_required,
             max(0) in_processing
              from ${MdTagDao.TABLE} mdt
            inner join ${MD_Schedule_ExecDao.TABLE} mse
                    on  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
             where 
            mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
            and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_WAITING_SYNC}')
            $scheduleFilter
            group by mdt.${MdTagDao.TAG_CODE} 
        union 
            select mdt.${MdTagDao.TAG_CODE} tag_operational_code, 
            mdt.${MdTagDao.TAG_DESC} tag_operational_desc, 
            count(mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}) qty,
            max((case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end)) update_required,
             max(0) sync_required,
             max(0) in_processing
              from ${MdTagDao.TABLE} mdt
            inner join ${MD_Schedule_ExecDao.TABLE} mse
                    on  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
             where 
            mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
            and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_WAITING_SYNC}')
            $scheduleNextFilter
            group by mdt.${MdTagDao.TAG_CODE} 
        union
            select gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_CODE}, 
                   gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_DESC}, 
                   count(gcdl.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_CODE}), 
            max((case when gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end)) update_required,
            max(0) sync_required,
            max((case when gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} =  '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                  then 1
                  else 0
            end)) in_processing
            from   ${GE_Custom_Form_LocalDao.TABLE} gcdl   
            where  gcdl.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = '$customerCode'
              and gcdl.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} in ('${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}', '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
              and schedule_prefix is null
              and schedule_code is null
              and schedule_exec is null
              and ticket_prefix is null
              and ticket_code is null
              and ticket_seq is null
              and ticket_seq_tmp is null
              and step_code is null 
            $formFilter
            )  ticket 
   where  ticket.tag_operational_code is not null
   group by ticket.tag_operational_code, ticket.tag_operational_desc;"""
        ).toString()
    }
}