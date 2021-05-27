package com.namoadigital.prj001.sql

import android.content.Context
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ConstantBaseApp.PREFERENCE_HOME_ONLY_MY_ACTIONS_OPTION
import com.namoadigital.prj001.util.ToolBox_Con

/**
 * BARRIONUEVO 20-05-2021
 *  - Query responsavel pelo extrato de tags no menu principal.
 *  - O concatenamento da tabela de schedule para pegar o proximo item além do filtro de data foi ne
 *  cessario devido restricoes da versao do sqlite no Android < 8
 */
class SqlAct005TagList001(private val context: Context,
                          private val customerCode: Int,
                          private val deviceGMT: String,
                          siteCode: Int,
                          periodFilter: String,
                          focusFilter: String
) : Specification {
    val customerGMT: String = ToolBox_Con.getPreference_Customer_TMZ(context)
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
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION ->
                """ and (strftime('%Y-%m-%d',ifnull(geap.${GE_Custom_Form_ApDao.AP_WHEN},geap.${GE_Custom_Form_ApDao.CREATE_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT'))"""
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION ->
                """ and (strftime('%Y-%m-%d',ifnull(geap.${GE_Custom_Form_ApDao.AP_WHEN},geap.${GE_Custom_Form_ApDao.CREATE_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','$deviceGMT', '+7 days'))"""
            else -> ""
        }
        //
        scheduleFilter = "  and (strftime('%s',mse.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') * 1000) < (strftime('%s','now','$deviceGMT')*1000)"
        //
        scheduleNextFilter = when (periodFilter) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "\n and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} ||  ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "\n and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} ||  ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
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
        val toString = sb.append(
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
                   ifnull(max(has_in_processing),0) in_processing 
            from ${TK_TicketDao.TABLE} tk, 
                 ${TK_Ticket_StepDao.TABLE} s
            left join (SELECT d.customer_code,
                              d.ticket_prefix,
                              d.ticket_code,
                              COUNT(1) has_in_processing
                      FROM ${GE_Custom_Form_DataDao.TABLE} d
                      WHERE d.customer_code = $customerCode 
                            and d.ticket_prefix is not null
                            and d.ticket_code is not null
                            and d.custom_form_status  = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                      GROUP BY
                             d.customer_code,
                             d.ticket_prefix,
                             d.ticket_code                                         
                     ) d on  tk.customer_code = d.customer_code
                             and tk.ticket_prefix = d.ticket_prefix
                             and tk.ticket_code = d.ticket_code 
            where  
                   tk.customer_code = s.customer_code
                   and tk.ticket_prefix = s.ticket_prefix
                   and tk.ticket_code = s.ticket_code 
                   and tk.current_step_order = s.step_order              
                   --
                   AND tk.customer_code = $customerCode                      
                   and tk.ticket_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}' , '${ConstantBaseApp.SYS_STATUS_PROCESS}' , '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                   and s.step_status in ('${ConstantBaseApp.SYS_STATUS_PENDING}',  '${ConstantBaseApp.SYS_STATUS_PROCESS}' )
                   $ticketFilter
        GROUP BY tk.tag_operational_code, 
                 tk.tag_operational_desc                   
        
        --UNION TICKET CACHE
        UNION ALL 
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
            where tk.ticket_code is null
                $ticketCacheFilter
            GROUP BY tkc.tag_operational_code, 
                   tkc.tag_operational_desc
        
        --UNION FORM AP                              
        UNION ALL 
            select  mdt.${MdTagDao.TAG_CODE} tag_operational_code, 
                    mdt.${MdTagDao.TAG_DESC} tag_operational_desc, 
                    count(geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}) qty,
                    max(geap.upload_required) update_required,
                    max(geap.sync_required) sync_required,
                    max(0) in_processing
            from ${MdTagDao.TABLE} mdt,
                 ${GE_Custom_Form_ApDao.TABLE} geap                    
            where
                 mdt.${MdTagDao.CUSTOMER_CODE} = geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE}
                 and mdt.${MdTagDao.TAG_CODE} = geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}
                 --
                 and geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
                 and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
                 $formApFilter
            GROUP BY mdt.${MdTagDao.TAG_CODE},   
                     mdt.${MdTagDao.TAG_DESC}
        -- UNION Agendamento
        UNION ALL 
            select mdt.${MdTagDao.TAG_CODE} tag_operational_code, 
            mdt.${MdTagDao.TAG_DESC} tag_operational_desc, 
            count(mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}) qty,
            max((case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                  then 1
                  else 0
            end)) update_required,
             max(0) sync_required,
             max((case when mse.${MD_Schedule_ExecDao.STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                  then 1
                  else 0
            end)) in_processing
              from ${MdTagDao.TABLE} mdt
            inner join ${MD_Schedule_ExecDao.TABLE} mse
                    on  mdt.${MdTagDao.CUSTOMER_CODE} = mse.${MD_Schedule_ExecDao.CUSTOMER_CODE}
                    and  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
             where 
            mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
            and mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = mdt.${MdTagDao.CUSTOMER_CODE}
            and mse.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_IN_PROCESSING}','${Constant.SYS_STATUS_WAITING_SYNC}')
            $scheduleFilter
             GROUP BY mdt.${MdTagDao.TAG_CODE},   
                     mdt.${MdTagDao.TAG_DESC}
        UNION ALL 
            select 
                s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} tag_operational_code, 
                s2.${MD_Schedule_ExecDao.TAG_OPERATIONAL_DESC} tag_operational_desc, 
                count(1) qty,
                max((case when s2.${MD_Schedule_ExecDao.STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                      then 1
                      else 0
                end)) update_required,
                 0 sync_required,
                 max((case when s2.${MD_Schedule_ExecDao.STATUS}  = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                      then 1
                      else 0
                end))  in_processing
                  from ${MD_Schedule_ExecDao.TABLE} s2
                  WHERE                   
                   s2.${MD_Schedule_ExecDao.SCHEDULE_PREFIX} ||substr('0000000000'||s2.${MD_Schedule_ExecDao.SCHEDULE_CODE},-10)||substr('0000000000'||s2.${MD_Schedule_ExecDao.SCHEDULE_EXEC},-10)
                   IN ( 
                        SELECT min(s1.${MD_Schedule_ExecDao.SCHEDULE_PREFIX} ||substr('0000000000'||s1.${MD_Schedule_ExecDao.SCHEDULE_CODE},-10)||substr('0000000000'||s1.${MD_Schedule_ExecDao.SCHEDULE_EXEC},-10)) schedule_pk                        
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
                                                                and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') * 1000) >= (strftime('%s','now','$deviceGMT')*1000)                                                                
                                                                $scheduleNextFilter
                                                                and s.${MD_Schedule_ExecDao.STATUS} IN ('${Constant.SYS_STATUS_SCHEDULE}','${Constant.SYS_STATUS_IN_PROCESSING}','${Constant.SYS_STATUS_PENDING}','${Constant.SYS_STATUS_WAITING_SYNC}')                 
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
         --UNION FORM AVULSO                     
            UNION ALL
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
       order by ticket.tag_operational_desc
                                                                """
        ).toString()
        return toString
    }
}