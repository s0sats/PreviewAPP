package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con

class SqlAct083_003(
        private val context: Context,
        private val originFlow: String,
        private val customerCode: Int,
        private var tagOperCode: Int?,
        private var productCode: Int?,
        private var serialId: String?,
        private var calendarDate: String?,
        val userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
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
        periodDateFilter =  when(ToolBox_Con.getStringPreferencesByKey(context, ConstantBaseApp.PREFERENCE_HOME_PERIOD_FILTER, ConstantBaseApp.PREFERENCE_HOME_ALL_TIME_OPTION)){
            ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> " and (strftime('%Y-%m-%d',a.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"'))"
            ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> " and (strftime('%Y-%m-%d',a.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') <= strftime('%Y-%m-%d','now','"+deviceGMT+"','+7 days'))"
            else -> ""
        }
        productCode = null
        serialId = null
        calendarDate = null
        statusFilter = when(userFocus){
            1 ->    """   and    a.${GE_Custom_Form_ApDao.AP_STATUS} in ('${ConstantBaseApp.SYS_STATUS_EDIT}',
                                                                 '${ConstantBaseApp.SYS_STATUS_PENDING}',
                                                                 '${ConstantBaseApp.SYS_STATUS_PROCESS}',                                                                 
                                                                 '${ConstantBaseApp.SYS_STATUS_WAITING_ACTION}'
                                                                 ) 
                                                                 """
            else -> """    and     a.${GE_Custom_Form_ApDao.AP_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     a.*
                    FROM 
                     ${GE_Custom_Form_ApDao.TABLE} a
                    WHERE
                     a.${GE_Custom_Form_ApDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter                                        
                     and ($tagOperCode is null or a.${GE_Custom_Form_ApDao.TAG_OPERATIONAL_CODE} = $tagOperCode) 
                     and ($productCode is null or a.${GE_Custom_Form_ApDao.PRODUCT_CODE} = $productCode )
                     and ('$serialId' is null or a.${GE_Custom_Form_ApDao.SERIAL_ID} = '$serialId' ) 
                     and ('$calendarDate' is null or strftime('%Y-%m-%d',a.${GE_Custom_Form_ApDao.AP_WHEN},'$deviceGMT') = '$calendarDate' )
                     $periodDateFilter
              """.replace("'null'","null")
        return s
    }
}