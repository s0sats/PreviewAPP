package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_005(
        private val context: Context,
        private val customerCode: Int,
        private val tagOperCode: Int?,
        private val productCode: Int?,
        private val serialId: String?,
        private val siteCode: Int?,
        private val calendarDate: String?
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private val customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context)
    private val dateFilter: String =
            when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_PERIOD_NEXT_ACTION_OPTION)){
                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
                else -> ""
            }

    override fun toSqlQuery(): String {
        return  """  SELECT
                             s.*,
                             1 line_number
                            FROM
                             ${MD_Schedule_ExecDao.TABLE} s
                            WHERE
                             s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode   
                             and ($calendarDate is null or (strftime('%Y-%m-%d',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT')) = $calendarDate)                             
                             and ($tagOperCode or s.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                             and ($siteCode is null or s.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode)
                             and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                             and ($serialId is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = $serialId )
                             and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START} || ' $customerGMT','$deviceGMT') * 1000) < (strftime('%s','now','$deviceGMT')*1000)
                             
                            UNION
                            
                            SELECT
                                T.*
                            FROM(                                  
                                  SELECT 
                                   s1.*,
                                   row_number() over (PARTITION BY  
                                                         s1.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                         s1.${MD_Schedule_ExecDao.SERIAL_ID},
                                                         s1.${MD_Schedule_ExecDao.SITE_CODE},
                                                         s1.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                         s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                         IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0) ,
                                                         IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0) ,
                                                         IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0) 
                                                     order by
                                                               s1.${MD_Schedule_ExecDao.SCHEDULE_PREFIX},s1.${MD_Schedule_ExecDao.SCHEDULE_CODE}                      
                                                       ) line_number
                                  FROM
                                   ${MD_Schedule_ExecDao.TABLE} s1
                                  WHERE
                                    (  s1.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                       s1.${MD_Schedule_ExecDao.SERIAL_ID},
                                       s1.${MD_Schedule_ExecDao.SITE_CODE},
                                       s1.${MD_Schedule_ExecDao.OPERATION_CODE},
                                       s1.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                       IFNULL(s1.${MD_Schedule_ExecDao.TICKET_TYPE},0) ,
                                       IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0) ,
                                       IFNULL(s1.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0) ,
                                       s1.${MD_Schedule_ExecDao.DATE_START} ) in  (SELECT
                                                             s.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                             s.${MD_Schedule_ExecDao.SERIAL_ID},
                                                             s.${MD_Schedule_ExecDao.SITE_CODE},
                                                             s.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                             s.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                             IFNULL(s.${MD_Schedule_ExecDao.TICKET_TYPE},0) ${MD_Schedule_ExecDao.TICKET_TYPE},
                                                             IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},0) ${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},
                                                             IFNULL(s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},0) ${MD_Schedule_ExecDao.CUSTOM_FORM_CODE},
                                                             min(s.${MD_Schedule_ExecDao.DATE_START}) ${MD_Schedule_ExecDao.DATE_START}                           
                                                            FROM
                                                             ${MD_Schedule_ExecDao.TABLE} s
                                                            WHERE
                                                                 s.${MD_Schedule_ExecDao.CUSTOMER_CODE} = $customerCode
                                                                 and ($tagOperCode or s.${MD_Schedule_ExecDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                                                                 and ($siteCode is null or s.${MD_Schedule_ExecDao.SITE_CODE} = $siteCode)
                                                                 and ($productCode is null or s.${MD_Schedule_ExecDao.PRODUCT_CODE} = $productCode )
                                                                 and ($serialId is null or s.${MD_Schedule_ExecDao.SERIAL_ID} = $serialId )
                                                                 and (strftime('%s',s.${MD_Schedule_ExecDao.DATE_START}||' $customerGMT','$deviceGMT') * 1000) >= (strftime('%s','now','$deviceGMT') * 1000)
                                                                 $dateFilter
                                                            GROUP BY
                                                                 s.${MD_Schedule_ExecDao.PRODUCT_CODE},
                                                                 s.${MD_Schedule_ExecDao.SERIAL_ID},
                                                                 s.${MD_Schedule_ExecDao.SITE_CODE},
                                                                 s.${MD_Schedule_ExecDao.OPERATION_CODE},
                                                                 s.${MD_Schedule_ExecDao.SCHEDULE_TYPE},
                                                                 s.${MD_Schedule_ExecDao.TICKET_TYPE},
                                                                 s.${MD_Schedule_ExecDao.CUSTOM_FORM_TYPE},
                                                                 s.${MD_Schedule_ExecDao.CUSTOM_FORM_CODE}
                                                     )
                            ) T
                            WHERE
                             T.LINE_NUMBER = 1         
                      """
    }
}