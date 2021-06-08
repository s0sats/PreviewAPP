package com.namoadigital.prj001.sql

import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct084_001(
        private val customerCode: Long,
        private val doneTab: Int,
        private val multStepsLbl:String?
) : Specification {
    private val INNER_UPDATE_REQUIRED = "INNER_UPDATE_REQUIRED"
    private val TOTAL_UPDATE_REQUIRED = "TOTAL_UPDATE_REQUIRED"
    private var statusFilter: String =""

    init {
        getStatusFilter()
    }

    private fun getStatusFilter() {
        statusFilter = when (doneTab) {
            1 ->  """    and t.${TK_TicketDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_DONE}'"""
            else -> """    and t.${TK_TicketDao.TICKET_STATUS} in( '${ConstantBaseApp.SYS_STATUS_NOT_EXECUTED}',
                                                                   '${ConstantBaseApp.SYS_STATUS_CANCELLED}',
                                                                   '${ConstantBaseApp.SYS_STATUS_ERROR}',               
                                                                   '${ConstantBaseApp.SYS_STATUS_REJECTED}'                
                                                                  ) """.trimMargin()
        }
    }

    override fun toSqlQuery(): String {
        var s = """SELECT
                     t.*,
                     0 ${MyActions.MY_ACTION_TYPE_FORM},
                     ts.${TK_Ticket_StepDao.STEP_DESC},                     
                     ts.${TK_Ticket_StepDao.FORECAST_START},                     
                     ts.${TK_Ticket_StepDao.FORECAST_END},
                     max(
                            t.${TK_TicketDao.UPDATE_REQUIRED},
                            t.${TK_TicketDao.UPDATE_REQUIRED_PRODUCT},
                            ifnull(ts.$INNER_UPDATE_REQUIRED,0)
                     ) ${TOTAL_UPDATE_REQUIRED}                                           
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
                         GROUP BY  
                           s.${TK_Ticket_StepDao.CUSTOMER_CODE},
                           s.${TK_Ticket_StepDao.TICKET_PREFIX},
                           s.${TK_Ticket_StepDao.TICKET_CODE},
                           s.${TK_Ticket_StepDao.STEP_ORDER}     
                        ) ts ON  ts.${TK_Ticket_StepDao.CUSTOMER_CODE} = t.${TK_TicketDao.CUSTOMER_CODE}
                                and ts.${TK_Ticket_StepDao.TICKET_PREFIX} = t.${TK_TicketDao.TICKET_PREFIX}
                                and ts.${TK_Ticket_StepDao.TICKET_CODE} = t.${TK_TicketDao.TICKET_CODE}
                                and ts.${TK_Ticket_StepDao.STEP_ORDER} = t.${TK_TicketDao.CURRENT_STEP_ORDER}                                
                    WHERE                     
                          t.${TK_TicketDao.CUSTOMER_CODE} = $customerCode
                          and t.${TK_Ticket_StepDao.TICKET_PREFIX} > 0 
                          $statusFilter                                                                                                       
              """.replace("'null'","null")
        return s
    }
}