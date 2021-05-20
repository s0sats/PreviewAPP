package com.namoadigital.prj001.sql

import android.content.Context
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.dao.TK_TicketDao.FORECAST_DATE
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION

/**
 * BARRIONUEVO 20-05-2021
 *  - Query responsavel pelo extrato de tags no menu principal.
 *  - O concatenamento da tabela de schedule para pegar o proximo item além do filtro de data foi ne
 *  cessario devido restricoes da versao do sqlite no Android < 8
 */
class SqlAct005TagList001(private val context: Context,
                          private val customerCode: Int,
                          deviceGMT: String,
                          siteCode: Int,
                          periodFilter: String,
                          focusFilter: String
) : Specification {
    var ticketFilter = ""
    var ticketCacheFilter = ""
    var formApFilter = ""
    var scheduleFilter = ""
    var scheduleNextFilter = ""
//    var formFilter = ""
    init{
        ticketFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',s.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
        //
        ticketCacheFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',tkc.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',tkc.${TK_Ticket_StepDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
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
        //
        scheduleNextFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') > strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') > strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> "\n and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START},'$deviceGMT') >= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
        }

        if (siteCode > 0) {
            ticketFilter += "\n and tk.${TK_TicketDao.OPEN_SITE_CODE} = $siteCode "
            ticketCacheFilter += "\n and tkc.${TkTicketCacheDao.OPEN_SITE_CODE} = $siteCode "
            scheduleFilter += "\n and mse.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode "
            scheduleNextFilter += "\n and s.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode "
//            formFilter += "\n and gcdl.${GE_Custom_Form_LocalDao.SITE_CODE} = $siteCode "
        }

        if (PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION.equals(focusFilter)) {
            ticketFilter += "\n and tk.${TK_TicketDao.USER_FOCUS} = 1 "
            ticketCacheFilter += "\n and tkc.${TkTicketCacheDao.USER_FOCUS} = 1 "
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
        GROUP BY tk.tag_operational_code, 
                 tk.tag_operational_desc
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
                $ticketCacheFilter
            GROUP BY tkc.tag_operational_code, 
                   tkc.tag_operational_desc
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
            GROUP BY mdt.${MdTagDao.TAG_CODE},   
                     mdt.${MdTagDao.TAG_DESC}
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
                    on  mdt.${MdTagDao.CUSTOMER_CODE} = mse.${MD_Schedule_ExecDao.CUSTOMER_CODE}
                    and  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
             where 
            mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
            and mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = mdt.${MdTagDao.CUSTOMER_CODE}
            and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_WAITING_SYNC}')
            $scheduleFilter
             GROUP BY mdt.${MdTagDao.TAG_CODE},   
                     mdt.${MdTagDao.TAG_DESC}
        union 
            select 
                s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} tag_operational_code, 
                s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_DESC} tag_operational_desc, 
                count(1) qty,
                max((case when s2.${MD_Schedule_ExecDao.STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                      then 1
                      else 0
                end)) update_required,
                 0 sync_required,
                 0 in_processing
                  from ${MD_Schedule_ExecDao.TABLE} s2
                  WHERE  s2.${MD_Schedule_ExecDao.SCHEDULE_PREFIX}||s2.${MD_Schedule_ExecDao.SCHEDULE_CODE}||s2.${MD_Schedule_ExecDao.SCHEDULE_EXEC}
                   IN ( 
                        SELECT min(s1.${MD_Schedule_ExecDao.SCHEDULE_PREFIX}||s1.${MD_Schedule_ExecDao.SCHEDULE_CODE}||s1.${MD_Schedule_ExecDao.SCHEDULE_EXEC}) schedule_pk
                          FROM  ${MD_Schedule_ExecDao.TABLE} s1
                         WHERE s1.${MD_Schedule_ExecDao.PRODUCT_CODE}||'_'||
                           s1.${MD_Schedule_ExecDao.SERIAL_ID}||'_'||
                           s1.${MD_Schedule_ExecDao.SITE_CODE}||'_'||
                           s1.${MD_Schedule_ExecDao.OPERATION_CODE}||'_'||
                           s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE}||'_'||
                           IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0)||'_'||
                           IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0)||'_'||
                           IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)||'_'||
                                           s1.${MD_Schedule_ExecDao.DATE_START}  in  (
                                                            SELECT
                                                                 max(s.${MD_Schedule_ExecDao.PRODUCT_CODE}||'_'||
                                                                     s.${MD_Schedule_ExecDao.SERIAL_ID}||'_'||
                                                                     s.${MD_Schedule_ExecDao.SITE_CODE}||'_'||
                                                                     s.${MD_Schedule_ExecDao.OPERATION_CODE}||'_'||
                                                                     s.${MD_Schedule_ExecDao.SCHEDULE_TYPE}||'_'||
                                                                     IFNULL(s.${MD_Schedule_ExecDao.TICKET_TYPE},0)||'_'||
                                                                     IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0)||'_'||
                                                                     IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0) 
                                                                     )||'_'||
                                                                 min(s.${MD_Schedule_ExecDao.DATE_START})                          
                                                                FROM
                                                                 ${MD_Schedule_ExecDao.TABLE} s
                                                                WHERE  s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode
                                                                $scheduleNextFilter
                                                                and s.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_PENDING}','${Constant.SYS_STATUS_WAITING_SYNC}')                 
                                                             GROUP BY
                                                                 s.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                                 s.${MD_Schedule_ExecDao.SERIAL_ID},
                                                                 s.${MD_Schedule_ExecDao.SITE_CODE},
                                                                 s.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                                 s.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                                 IFNULL(s.${MD_Schedule_ExecDao.TICKET_TYPE},0),
                                                                 IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0),
                                                                 IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)
                                                                    )
                        GROUP BY   s1.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                   s1.${MD_Schedule_ExecDao.SERIAL_ID},
                                   s1.${MD_Schedule_ExecDao.SITE_CODE},
                                   s1.${MD_Schedule_ExecDao.OPERATION_CODE},
                                   s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                   IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0),
                                   IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0),
                                   IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)
                   
                   )   
            GROUP BY s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE},   
                     s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_DESC}
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
              and gcdl.schedule_prefix is null
              and gcdl.schedule_code is null
              and gcdl.schedule_exec is null
              and gcdl.ticket_prefix is null
              and gcdl.ticket_code is null
              and gcdl.ticket_seq is null
              and gcdl.ticket_seq_tmp is null
              and gcdl.step_code is null 
            GROUP BY gcdl.tag_operational_code, 
                   gcdl.tag_operational_desc                  
                         )  ticket 
   where  ticket.tag_operational_code is not null
   group by ticket.tag_operational_code, ticket.tag_operational_desc
                                                                """
        ).toString()
    }
}