package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_005(
        private val context: Context,
        private val originFlow: String,
        private val customerCode: Int,
        private var tagOperCode: Int?,
        private var productCode: Int?,
        private var serialId: String?,
        private var siteCode: String?,
        private var calendarDate: String?,
        private var userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private val customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context)
    private var periodDateFilter: String =""
    private var statusFilter = ""

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
                                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
                                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
                                else -> ""
                            }
        productCode = null
        serialId = null
        calendarDate = null
        statusFilter = when(userFocus){
                        1 -> """    and     s.${MD_Schedule_ExecDao.STATUS} in('${ConstantBaseApp.SYS_STATUS_SCHEDULE}','${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}','${ConstantBaseApp.SYS_STATUS_IN_PROCESSING}') """
                        else -> """    and     s.${MD_Schedule_ExecDao.STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }

    }

    override fun toSqlQuery(): String {
        var s = """  SELECT
                             s.*
                            FROM
                             ${MD_Schedule_ExecDao.TABLE} s
                            WHERE
                             s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode   
                             $statusFilter
                             and ('$calendarDate' is null or (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT')) = '$calendarDate')                             
                             and ($tagOperCode is null or s.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                             and ($siteCode is null or s.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode)
                             and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                             and ('$serialId' is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = '$serialId' )
                             and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') * 1000) < (strftime('%s','now','$deviceGMT')*1000)
                             
                            UNION
                            
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
                                           s1.${MD_Schedule_ExecDao.SERIAL_ID}||'|'||
                                           s1.${MD_Schedule_ExecDao.SITE_CODE}||'|'||
                                           s1.${MD_Schedule_ExecDao.OPERATION_CODE}||'|'||
                                           s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE}||'|'||
                                           IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0)||'|'||
                                           IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0)||'|'||
                                           IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)||'|'||
                                           s1.${MD_Schedule_ExecDao.DATE_START} ) in  (SELECT
                                                                 s.${MD_Schedule_ExecDao.PRODUCT_CODE}||'|'||
                                                                 s.${MD_Schedule_ExecDao.SERIAL_ID}||'|'||
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
                                                                     and ($tagOperCode is null or s.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                                                                     and ($siteCode is null or s.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode)
                                                                     and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                                                                     and ('$serialId' is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = '$serialId')
                                                                     and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START}||' $customerGMT','$deviceGMT') * 1000) >= (strftime('%s','now','$deviceGMT') * 1000)
                                                                     $periodDateFilter
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
                                      GROUP BY
                                         s1.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                         s1.${MD_Schedule_ExecDao.SERIAL_ID},
                                         s1.${MD_Schedule_ExecDao.SITE_CODE},
                                         s1.${MD_Schedule_ExecDao.OPERATION_CODE},
                                         s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                         IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0),
                                         IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0),
                                         IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0)                   
                                )      
                      """.replace("'null'","null")
        return s
    }
}