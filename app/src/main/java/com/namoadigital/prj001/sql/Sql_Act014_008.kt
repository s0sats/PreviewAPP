package com.namoadigital.prj001.sql

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.ui.act014.Act014_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp

/**
 * BARRIONUEVO 01-06-2021
 * Query de historico de minhas ações. (TICKET+ FORM + FORM-AP
 */
class Sql_Act014_008(
        val customerCode: Long,
        val label_translation: HMAux) : Specification {
    val SENT_QTY = "sent_qty"
    val TYPE = "type"
    override fun toSqlQuery(): String {
        val sb = StringBuilder()

        val result = sb
                .append(""" 
 SELECT
     ifnull(sum(${SENT_QTY}), 0) $SENT_QTY,
     '${label_translation[Act014_Main.LABEL_TRANS_MY_ACTION]}' $TYPE
    FROM (
         SELECT
             count(1) ${SENT_QTY}    
         FROM  ${GE_Custom_Form_ApDao.TABLE} a
         WHERE a.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = '$customerCode' 
           AND a.${GE_Custom_Form_ApDao.AP_STATUS}    in ('${Constant.SYS_STATUS_DONE}','${Constant.SYS_STATUS_CANCELLED}')
               
         union all    
         SELECT
             count(1) ${SENT_QTY}
         FROM  ${GE_Custom_Form_LocalDao.TABLE} l
          WHERE l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = '$customerCode' 
            AND l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} in (
                                                '${Constant.SYS_STATUS_DONE}',
                                                '${Constant.SYS_STATUS_NOT_EXECUTED}',
                                                '${Constant.SYS_STATUS_CANCELLED}',
                                                '${Constant.SYS_STATUS_IGNORED}'
                                                )
         union all
                
            SELECT
                 count(1) ${SENT_QTY} 
              FROM  ${TK_TicketDao.TABLE} s
             WHERE s.customer_code = '$customerCode'
               and s.ticket_status in('${ConstantBaseApp.SYS_STATUS_DONE}'
            ,'${ConstantBaseApp.SYS_STATUS_NOT_EXECUTED}'
            ,'${ConstantBaseApp.SYS_STATUS_CANCELLED}'
            ,'${ConstantBaseApp.SYS_STATUS_IGNORED}'
            ,'${ConstantBaseApp.SYS_STATUS_REJECTED}') 
    )
;""")
                .toString()
        return result
    }

}