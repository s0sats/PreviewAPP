package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

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
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
    private var statusFilter = ""
    private var byPassByOpenForm = " 1 = 1"

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
        periodDateFilter = when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)){
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and  (strftime('%Y-%m-%d', ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and  (strftime('%Y-%m-%d', ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}),'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
            else -> ""
        }
        productCode = null
        serialId = null
        clientId = null
        contractId = null
        calendarDate = null
        getStatusFilter()
        byPassByOpenForm = """ EXISTS (  SELECT 1
                                        FROM ${GE_Custom_Form_LocalDao.TABLE} l
                                        WHERE l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                and l.${GE_Custom_Form_LocalDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                                )
                                """
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
                    LEFT JOIN    
                        (SELECT
                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                           s.${TK_Ticket_StepDao.TICKET_CODE},
                           s.${TK_Ticket_StepDao.STEP_ORDER},
                           s.${TK_Ticket_StepDao.USER_FOCUS},
                           (case when s.${TK_Ticket_StepDao.USER_FOCUS} = 0
                                 then null   
                                 when count(1) = 1
                                 then min(s.${TK_Ticket_StepDao.STEP_DESC})
                                 else '$multStepsLbl'
                           end) ${TK_Ticket_StepDao.STEP_DESC} ,
                           min(s.forecast_start) ${TK_Ticket_StepDao.FORECAST_START},
                           max(s.forecast_end) ${TK_Ticket_StepDao.FORECAST_END},
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
                            and ($userFocus = 0 or s.${TK_Ticket_StepDao.USER_FOCUS} = 1)
                         GROUP BY  
                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                           s.${TK_Ticket_StepDao.TICKET_CODE},
                           s.${TK_Ticket_StepDao.STEP_ORDER}     
                        ) ts ON  ts.${TK_Ticket_StepDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                and ts.${TK_Ticket_StepDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                and ts.${TK_Ticket_StepDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                and ts.${TK_Ticket_StepDao.STEP_ORDER} = t.${TK_TicketDao.CURRENT_STEP_ORDER}
                                and ts.${TK_Ticket_StepDao.USER_FOCUS} = $userFocus
                    WHERE                     
                          t.${TK_TicketDao.CUSTOMER_CODE} = $customerCode
                          and t.${TK_Ticket_StepDao.TICKET_PREFIX} > 0 
                          $statusFilter                          
                          and ('$calendarDate' is null or strftime('%Y-%m-%d',ifnull(ts.${TK_Ticket_StepDao.FORECAST_START}, t.${TK_TicketDao.FORECAST_DATE}), '$deviceGMT') = '$calendarDate' )                          
                          and ($byPassByOpenForm or ($tagOperCode is null or t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode))
                          and ($siteCode is null or t.${TK_TicketDao.OPEN_SITE_CODE}  = $siteCode)
                          and ($productCode is null or t.${TK_TicketDao.OPEN_PRODUCT_CODE}  = $productCode )
                          and ('$serialId' is null or t.${TK_TicketDao.OPEN_SERIAL_ID}  = '$serialId' )
                          and ('$clientId' is null or t.${TK_TicketDao.CLIENT_ID}  = '$clientId')
                          and ('$contractId' is null or t.${TK_TicketDao.CONTRACT_ID}  = '$contractId')
                          and ('$ticketId' is null or t.${TK_TicketDao.TICKET_ID}  = '$ticketId')                         
                          $periodDateFilter                                    
              """.replace("'null'","null")
        return s
    }
}