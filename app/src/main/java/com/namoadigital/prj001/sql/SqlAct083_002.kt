package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TK_Ticket_StepDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
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
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
    private var statusFilter = ""
    private var byPassByOpenForm = " 1 = 1"

    init {
        setFiltersByOriginAndFocus()
    }

    private fun setFiltersByOriginAndFocus() {
        when(originFlow){
            ConstantBaseApp.ACT005 -> setHomeFilterConfg()
        }
    }

    private fun setHomeFilterConfg() {
        periodDateFilter = when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)){
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and  (strftime('%Y-%m-%d',t.${TK_TicketDao.FORECAST_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and  (strftime('%Y-%m-%d',t.${TK_TicketDao.FORECAST_DATE},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
            else -> ""
        }
        productCode = null
        serialId = null
        clientId = null
        contractId = null
        calendarDate = null
        statusFilter = when(userFocus){
            1 -> """    and  t.${TK_TicketDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}')                 
                        and ($userFocus is null or t.${TK_TicketDao.USER_FOCUS} = $userFocus)
                 """
            else -> """    and (t.${TK_TicketDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'
                           OR ($userFocus is null or t.${TK_TicketDao.USER_FOCUS} = $userFocus))
                    """
        }
        byPassByOpenForm = """ EXISTS (  SELECT 1
                                        FROM ${GE_Custom_Form_LocalDao.TABLE} l
                                        WHERE l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                and l.${GE_Custom_Form_LocalDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                and l.${GE_Custom_Form_LocalDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}'
                                )
                                """
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
                     ts.${TK_Ticket_StepDao.STEP_DESC}
                    FROM
                        ${TK_TicketDao.TABLE} t
                    LEFT JOIN    
                        (SELECT
                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                           s.${TK_Ticket_StepDao.TICKET_CODE},
                           s.${TK_Ticket_StepDao.STEP_ORDER},
                           s.${TK_Ticket_StepDao.USER_FOCUS},
                           (case when count(1) = 1
                                 then min(s.${TK_Ticket_StepDao.STEP_DESC})
                                 else '$multStepsLbl'
                           end) ${TK_Ticket_StepDao.STEP_DESC}                           
                         FROM
                           ${TK_Ticket_StepDao.TABLE} s
                         WHERE  
                           s.${TK_Ticket_StepDao.CUSTOMER_CODE} = ${customerCode}
                           and s.${TK_Ticket_StepDao.USER_FOCUS} = 1
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
                          $statusFilter                          
                          and ($calendarDate is null or strftime('%Y-%m-%d', t.${TK_TicketDao.FORECAST_DATE}, '$deviceGMT') = $calendarDate )                          
                          and ($byPassByOpenForm or ($tagOperCode is null or t.${TK_TicketDao.TAG_OPERATIONAL_CODE} = $tagOperCode))
                          and ($siteCode is null or t.${TK_TicketDao.OPEN_SITE_CODE}  = $siteCode)
                          and ($productCode is null or t.${TK_TicketDao.OPEN_PRODUCT_CODE}  = $productCode )
                          and ($serialId is null or t.${TK_TicketDao.OPEN_SERIAL_ID}  = $serialId )
                          and ($clientId is null or t.${TK_TicketDao.CLIENT_ID}  = $clientId)
                          and ($contractId is null or t.${TK_TicketDao.CONTRACT_ID}  = $contractId)
                          and ($ticketId is null or t.${TK_TicketDao.TICKET_ID}  = $ticketId)                         
                          $periodDateFilter                                    
              """
        return s
    }
}