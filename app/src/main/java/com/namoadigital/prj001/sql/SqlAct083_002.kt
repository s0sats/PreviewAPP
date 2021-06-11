package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SqlAct083_002(
        private val context: Context,
        private val originFlow: String,
        private val customerCode: Int,
        private var tagOperCode: Int?,
        private var siteCode: String?,
        private var productCode: Int?,
        private var serialId: String?,
        private var clientId: String?,
        private var contractId: String?,
        private var ticketId: String?,
        private var calendarDate: String?,
        private var userFocus: Int?,
        private val multStepsLbl: String?
) : Specification {

    private val INNER_UPDATE_REQUIRED = "INNER_UPDATE_REQUIRED"
    private val FORECAST_START_FOCUS = "FORECAST_START_FOCUS"
    private val FORECAST_START_N_FOCUS = "FORECAST_START_N_FOCUS"
    private val FORECAST_END_FOCUS = "FORECAST_END_FOCUS"
    private val FORECAST_END_N_FOCUS = "FORECAST_END_N_FOCUS"
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
    private var statusFilter = ""
    private var byPassByOpenForm = ""

    companion object{
        const val TOTAL_UPDATE_REQUIRED = "TOTAL_UPDATE_REQUIRED"
    }

    init {
        setFiltersByOriginAndFocus()
    }

    private fun setFiltersByOriginAndFocus() {
        when(originFlow){
            ConstantBaseApp.ACT005 -> setHomeFilterConfg()
            ConstantBaseApp.ACT006 -> setSerialFilterConfg()
            ConstantBaseApp.ACT016 -> setCalendarFilterConfg()
            ConstantBaseApp.ACT068 -> setMenuSearchFilterConfig()
        }
    }

    private fun setHomeFilterConfg() {
        periodDateFilter = getPeriodFilter()
        productCode = null
        serialId = null
        clientId = null
        contractId = null
        calendarDate = null
        getStatusFilter()
        byPassByOpenForm = """ or ( EXISTS (SELECT 1
                                            FROM ${GE_Custom_Form_LocalDao.TABLE} l
                                            WHERE   l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                                    and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                                    and l.${GE_Custom_Form_LocalDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                                    and t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode
                                                    and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                                           )
                                    )        
                                """
    }

    private fun getPeriodFilter() = if (!ToolBox_Inf.usesSoMainActivity(context)) {
        when (ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)) {
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and  (strftime('%Y-%m-%d', ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and  (strftime('%Y-%m-%d', ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
            else -> ""
        }
    } else {
        ""
    }

    private fun setSerialFilterConfg() {
        tagOperCode = null
        periodDateFilter = ""
        siteCode = null
        clientId = null
        contractId = null
        calendarDate = null
        getStatusFilter()
    }

    private fun setCalendarFilterConfg() {
        tagOperCode = null
        periodDateFilter = ""
        siteCode = null
        productCode = null
        serialId = null
        clientId = null
        contractId = null
        getStatusFilter()
    }

    private fun setMenuSearchFilterConfig() {
        tagOperCode = null
        periodDateFilter = ""
        siteCode = null
        productCode = null
        serialId = null
        calendarDate = null
        getStatusFilter()
    }

    private fun getStatusFilter() {
        statusFilter = when (userFocus) {
            1 -> """    and  t.${TK_TicketDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')                 
                            and ($userFocus is null or t.${TK_TicketDao.USER_FOCUS} = $userFocus)
                     """
            else -> """    and (t.${TK_TicketDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                                    OR (
                                        ($userFocus is null or t.${TK_TicketDao.USER_FOCUS} = $userFocus)
                                        and t.${TK_TicketDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}', '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                                    )
                               )
                        """
        }
    }

    override fun toSqlQuery(): String {
        var s = """SELECT
                     t.*,
                     (  SELECT IFNULL(count(1),0) 
                            FROM ${GE_Custom_Form_LocalDao.TABLE} l
                            WHERE   l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                    and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                    and l.${GE_Custom_Form_LocalDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                    and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                     ) ${MyActions.MY_ACTION_TYPE_FORM},
                     ts.${TK_Ticket_StepDao.STEP_DESC},                     
                     ts.${TK_Ticket_StepDao.FORECAST_START},                     
                     ts.${TK_Ticket_StepDao.FORECAST_END},
                     max(
                            t.${TK_TicketDao.UPDATE_REQUIRED},
                            t.${TK_TicketDao.UPDATE_REQUIRED_PRODUCT},
                            ifnull(ts.$INNER_UPDATE_REQUIRED,0)
                     ) $TOTAL_UPDATE_REQUIRED                                           
                    FROM
                        ${TK_TicketDao.TABLE} t
                    LEFT JOIN (SELECT
                                    ts.${TK_Ticket_StepDao.CUSTOMER_CODE},
                                    ts.${TK_Ticket_StepDao.TICKET_PREFIX},
                                    ts.${TK_Ticket_StepDao.TICKET_CODE},
                                    ts.${TK_Ticket_StepDao.STEP_ORDER}, 
                                    ts.${TK_Ticket_StepDao.STEP_DESC},
                                    IFNULL($FORECAST_START_FOCUS,$FORECAST_START_N_FOCUS) ${TK_Ticket_StepDao.FORECAST_START},
                                    IFNULL($FORECAST_END_FOCUS,$FORECAST_END_N_FOCUS) ${TK_Ticket_StepDao.FORECAST_END},
                                    $INNER_UPDATE_REQUIRED
                               FROM 
                                    (SELECT
                                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                                           s.${TK_Ticket_StepDao.TICKET_CODE},
                                           s.${TK_Ticket_StepDao.STEP_ORDER},
                                           s.${TK_Ticket_StepDao.USER_FOCUS},
                                           (case when s.${TK_Ticket_StepDao.USER_FOCUS} = 0
                                                 then null   
                                                 when count(distinct(s.${TK_Ticket_StepDao.STEP_CODE})) = 1
                                                 then min(s.${TK_Ticket_StepDao.STEP_DESC})
                                                 else '$multStepsLbl'
                                           end) ${TK_Ticket_StepDao.STEP_DESC} ,
                                           MIN(CASE WHEN s.${TK_Ticket_StepDao.USER_FOCUS} = 0
                                                    THEN null
                                                    WHEN s.${TK_Ticket_StepDao.STEP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                                                    THEN s.forecast_start
                                                    else null
                                               END) $FORECAST_START_FOCUS,
                                               
                                           MAX(CASE WHEN s.${TK_Ticket_StepDao.USER_FOCUS} = 0
                                                    THEN null
                                                    WHEN s.${TK_Ticket_StepDao.STEP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                                                    THEN  s.forecast_end
                                                    else null
                                               END) $FORECAST_END_FOCUS,
                                               
                                           MIN(CASE WHEN s.${TK_Ticket_StepDao.USER_FOCUS} = 1
                                                    THEN null                                           
                                                    WHEN s.${TK_Ticket_StepDao.STEP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                                                    THEN s.forecast_start
                                                    else null
                                               END) $FORECAST_START_N_FOCUS,
                                               
                                           MAX(CASE WHEN s.${TK_Ticket_StepDao.USER_FOCUS} = 1
                                                    THEN null 
                                                    WHEN s.${TK_Ticket_StepDao.STEP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')
                                                    THEN  s.forecast_end
                                                    else null
                                               END) $FORECAST_END_N_FOCUS,
                                           ifnull(
                                                    max(s.${TK_Ticket_StepDao.UPDATE_REQUIRED},
                                                        c.${TK_Ticket_CtrlDao.UPDATE_REQUIRED}
                                                    ),0) $INNER_UPDATE_REQUIRED  
                                         FROM
                                           ${TK_Ticket_StepDao.TABLE} s,
                                           ${TK_Ticket_CtrlDao.TABLE} c
                                         WHERE  
                                            s.${TK_Ticket_StepDao.CUSTOMER_CODE} = c.${TK_Ticket_CtrlDao.CUSTOMER_CODE}
                                            and s.${TK_Ticket_StepDao.TICKET_PREFIX} = c.${TK_Ticket_CtrlDao.TICKET_PREFIX}
                                            and s.${TK_Ticket_StepDao.TICKET_CODE} = c.${TK_Ticket_CtrlDao.TICKET_CODE}
                                            and s.${TK_Ticket_StepDao.STEP_CODE} = c.${TK_Ticket_CtrlDao.STEP_CODE}
                                            and s.${TK_Ticket_StepDao.CUSTOMER_CODE} = $customerCode                                                                      
                                         GROUP BY  
                                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                                           s.${TK_Ticket_StepDao.TICKET_CODE},
                                           s.${TK_Ticket_StepDao.STEP_ORDER} 
                                    ) ts              
                        ) ts ON  ts.${TK_Ticket_StepDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                and ts.${TK_Ticket_StepDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                and ts.${TK_Ticket_StepDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                and ts.${TK_Ticket_StepDao.STEP_ORDER} = t.${TK_TicketDao.CURRENT_STEP_ORDER}                                
                    WHERE                     
                          t.${TK_TicketDao.CUSTOMER_CODE} = $customerCode
                          and t.${TK_Ticket_StepDao.TICKET_PREFIX} > 0 
                          $statusFilter                          
                          and ('$calendarDate' is null or strftime('%Y-%m-%d',ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}), '$deviceGMT') = '$calendarDate' )                          
                          and ($tagOperCode is null or t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                          and (
                                (
                                    ($siteCode is null or t.${TK_TicketDao.OPEN_SITE_CODE}  = $siteCode)
                                    and ($productCode is null or t.${TK_TicketDao.OPEN_PRODUCT_CODE}  = $productCode )
                                    and ('$serialId' is null or t.${TK_TicketDao.OPEN_SERIAL_ID}  = '$serialId' )
                                    and ('$clientId' is null or t.${TK_TicketDao.CLIENT_ID}  = '$clientId')
                                    and ('$contractId' is null or t.${TK_TicketDao.CONTRACT_ID}  = '$contractId')
                                    and ('$ticketId' is null or t.${TK_TicketDao.TICKET_ID}  = '$ticketId')                         
                                    $periodDateFilter 
                                ) $byPassByOpenForm
                              )
              """.replace("'null'","null")
        return s
    }
}