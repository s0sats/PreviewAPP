package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SqlAct092_005(
    private val context: Context,
    private val customerCode: Int,
    private var productCode: Int?,
    private var serialId: String?,
    private var userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private val customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context)
    private var periodDateFilter: String =""
    private var statusFilter = ""
    private var lateFilter = ""
    private var nextEventFilter = ""

    init {
        setSerialFilterConfg()
    }

    private fun getNextEventJoin() {
        nextEventFilter = """ UNION                                
                                    SELECT
                                        s2.*
                                    FROM
                                        ${MD_Schedule_ExecDao.TABLE} s2
                                    WHERE
                                      s2.${MD_Schedule_ExecDao.SCHEDULE_PREFIX} ||substr('0000000000'||s2.${MD_Schedule_ExecDao.SCHEDULE_CODE},-10)||substr('0000000000'||s2.${MD_Schedule_ExecDao.SCHEDULE_EXEC},-10)
                                      IN (    SELECT 
                                                min(s1.${MD_Schedule_ExecDao.SCHEDULE_PREFIX} ||substr('0000000000'||s1.${MD_Schedule_ExecDao.SCHEDULE_CODE},-10)||substr('0000000000'||s1.${MD_Schedule_ExecDao.SCHEDULE_EXEC},-10)) schedule_pk
                                              FROM
                                               ${MD_Schedule_ExecDao.TABLE} s1
                                              WHERE
                                                (  s1.${MD_Schedule_ExecDao.PRODUCT_CODE}||'|'||
                                                   IFNULL(s1.${MD_Schedule_ExecDao.SERIAL_ID},0)||'|'||
                                                   s1.${MD_Schedule_ExecDao.SITE_CODE}||'|'||
                                                   s1.${MD_Schedule_ExecDao.OPERATION_CODE}||'|'||
                                                   s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE}||'|'||
                                                   IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0)||'|'||
                                                   IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0)||'|'||
                                                   IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)||'|'||
                                                   s1.${MD_Schedule_ExecDao.DATE_START} ) in  (SELECT
                                                                         s.${MD_Schedule_ExecDao.PRODUCT_CODE}||'|'||
                                                                         IFNULL(s.${MD_Schedule_ExecDao.SERIAL_ID},0)||'|'||
                                                                         s.${MD_Schedule_ExecDao.SITE_CODE}||'|'||
                                                                         s.${MD_Schedule_ExecDao.OPERATION_CODE}||'|'||
                                                                         s.${MD_Schedule_ExecDao.SCHEDULE_TYPE}||'|'||
                                                                         IFNULL(s.${MD_Schedule_ExecDao.TICKET_TYPE},0)||'|'||
                                                                         IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0)||'|'||
                                                                         IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)||'|'||
                                                                         min(s.${MD_Schedule_ExecDao.DATE_START}) ${MD_Schedule_ExecDao.DATE_START}                           
                                                                        FROM
                                                                         ${MD_Schedule_ExecDao.TABLE} s
                                                                        WHERE
                                                                             s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode
                                                                             $statusFilter
                                                                             and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                                                                             and ('$serialId' is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = '$serialId')
                                                                             and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START}||' $customerGMT','$deviceGMT') * 1000) >= (strftime('%s','now','$deviceGMT') * 1000)
                                                                             $periodDateFilter
                                                                        GROUP BY
                                                                             s.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                                             IFNULL(s.${MD_Schedule_ExecDao.SERIAL_ID},0),
                                                                             s.${MD_Schedule_ExecDao.SITE_CODE},
                                                                             s.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                                             s.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                                             IFNULL(s.${MD_Schedule_ExecDao.TICKET_TYPE},0),
                                                                             IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0),
                                                                             IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)
                                                                 )
                                              GROUP BY
                                                 s1.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                 IFNULL(s1.${MD_Schedule_ExecDao.SERIAL_ID},0),
                                                 s1.${MD_Schedule_ExecDao.SITE_CODE},
                                                 s1.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                 s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                 IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0),
                                                 IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0),
                                                 IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)                   
                                        )    """
    }


    private fun getPeriodFilter(): String {
        return if (!ToolBox_Inf.hasSoOrIOProfile(context)) {
            when(ToolBox_Inf.getActionTimeDefaultOption(context)){
                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
                else -> ""
            }
        }else{
            ""
        }
    }

    private fun getStatusFilter() {
        statusFilter = when (userFocus) {
            1 -> """    and     s.${MD_Schedule_ExecDao.STATUS} in('${ConstantBaseApp.SYS_STATUS_SCHEDULE}','${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}') """
            else -> """    and     s.${MD_Schedule_ExecDao.STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    private fun setSerialFilterConfg() {
        periodDateFilter = ""
        /**
         * BARRIONUEVO 01-04-2022
         * Quando fluxo via serial as acoes devem ser limitadas para ateh hoje.
         */
        if (!ToolBox_Inf.hasSoOrIOProfile(context)) {
            if (ToolBox_Inf.profileExists(
                    context,
                    ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL,
                    ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL_SCHEDULE_UNTIL_TODAY
                )
            ) {
                periodDateFilter = " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
            }
        }
        //
        lateFilter = """        and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') * 1000) < (strftime('%s','now','$deviceGMT')*1000)  """
        getStatusFilter()
        getNextEventJoin()
    }

    override fun toSqlQuery(): String {
        val s = """  SELECT
                             s.*
                            FROM
                             ${MD_Schedule_ExecDao.TABLE} s
                            WHERE
                             s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode   
                             $statusFilter                              
                             and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                             and ('$serialId' is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = '$serialId' )
                             $lateFilter
                             $nextEventFilter                                
                      """.replace("'null'","null")
        return s
    }
}