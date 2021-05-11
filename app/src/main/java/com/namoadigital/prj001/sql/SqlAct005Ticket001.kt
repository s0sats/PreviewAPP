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
                "select ticket.tag_operational_code tag_operational_code, \n" +
                        "  ticket.tag_operational_desc tag_operational_desc, \n" +
                        "  sum(qty) qty, \n" +
                        "  max(ticket.update_required) update_required, \n" +
                        "  max(ticket.sync_required) sync_required,\n" +
                        "  max(ticket.in_processing) in_processing\n" +
                        "from " +
                        "\n(select tk.tag_operational_code, \n" +
                        "  tk.tag_operational_desc , \n" +
                        "  count(tk.tag_operational_code) qty, \n" +
                        "  max( max(tk.update_required), max(tk.update_required_product)) update_required, \n" +
                        "  max(tk.sync_required) sync_required,\n" +
                        "   0 in_processing \n"+
                        "from ${TK_TicketDao.TABLE} tk \n" +
                        " JOIN\n" +
                        "     tk_ticket_step s ON \n" +
                        "       tk.customer_code = s.customer_code \n" +
                        "       AND tk.ticket_code = s.ticket_code \n" +
                        "       AND tk.ticket_prefix = s.ticket_prefix\n" +
                        "where tk.customer_code = $customerCode \n" +
                        " and s.step_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "',  \n" +
                        " '" + ConstantBaseApp.SYS_STATUS_PROCESS + "' ) \n" +
                        " and tk.ticket_status in ('" + ConstantBaseApp.SYS_STATUS_PENDING + "' , '" +
                        ConstantBaseApp.SYS_STATUS_PROCESS + "' , '" +
                        ConstantBaseApp.SYS_STATUS_WAITING_SYNC +
                        "')  \n" +
                        " and tk.current_step_order = s.step_order\n" +
                        " and tk.ticket_code = s.ticket_code \n" +
                        " AND tk.ticket_prefix = s.ticket_prefix\n" +
                        ticketFilter+
                        "\n" +
                        "union \n" +
                        "select tkc.tag_operational_code, \n" +
                        "  tkc.tag_operational_desc, \n" +
                        "  count(tkc.tag_operational_code) qty, \n" +
                        "  max(0) update_required, \n" +
                        "  max(0) sync_required, \n" +
                        "   0 in_processing \n"+
                        "from ${TkTicketCacheDao.TABLE} tkc\n" +
                        "left join tk_ticket tk\n" +
                        "     on tk.ticket_prefix = tkc.ticket_prefix \n" +
                        "    and tk.ticket_code = tkc.ticket_code\n" +
                        "    and tk.ticket_id = tkc.ticket_id\n" +
                        "where tk.ticket_code is null\n" +
                        """ union 
                        select mdt.${MdTagDao.TAG_CODE}, 
                        mdt.${MdTagDao.TAG_DESC}, 
                        count(geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}) qty,
                         0 updated_required,
                         0 sync_required,
                         0 in_processing
                          from ${MdTagDao.TABLE} mdt
                        inner join ${GE_Custom_Form_ApDao.TABLE} geap
                                on  mdt.${MdTagDao.TAG_CODE} = geap.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE}
                         where 
                        geap.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode'
                        and geap.${GE_Custom_Form_ApDao.AP_STATUS} not in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
                        
                        group by mdt.${MdTagDao.TAG_CODE} """
                        +
                        """ union 
                        select mdt.${MdTagDao.TAG_CODE}, 
                        mdt.${MdTagDao.TAG_DESC}, 
                        count(mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}) qty,
                         0 updated_required,
                         0 sync_required,
                         0 in_processing
                          from ${MdTagDao.TABLE} mdt
                        inner join ${MD_Schedule_ExecDao.TABLE} mse
                                on  mdt.${MdTagDao.TAG_CODE} = mse.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE}
                         where 
                        mse.${MD_Schedule_ExecDao.CUSTOMER_CODE} = '$customerCode'
                        and mse.${MD_Schedule_ExecDao.STATUS} = '${Constant.SYS_STATUS_SCHEDULE}'
                        $scheduleFilter
                        group by mdt.${MdTagDao.TAG_CODE} """
                        +
                        """union
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
                        
                        """ +
                        ")" +
                        "\nticket" +
                        "\nwhere  ticket.tag_operational_code is not null" +
                        "\ngroup by ticket.tag_operational_code, ticket.tag_operational_desc"


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
        )
                .append(";")
                .toString()
    }
}