package com.namoadigital.prj001.sql

import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.util.ConstantBaseApp

class SqlAct083_004(
        private val originFlow: String,
        private val customerCode: Int,
        private var tagOperCode: Int?,
        private var productCode: Int?,
        private var serialId: String?,
        private var calendarDate: String?,
        private var formLabel: String,
        private var userFocus: Int
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
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
        productCode = null
        serialId = null
        calendarDate = null
        statusFilter = when(userFocus){
                 1 -> """    and     l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS} = '${ConstantBaseApp.SYS_STATUS_PROCESS}' """
                 else -> """    and     l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS},
                     d.${GE_Custom_Form_DataDao.DATE_START},
                     l.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_DESC},
                     l.${GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_DESC},
                     l.${GE_Custom_Form_LocalDao.SERIAL_ID},                     
                     '$formLabel' ${MyActions.MY_ACTION_TYPE_FORM},
                     l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DESC},
                     l.${GE_Custom_Form_LocalDao.SITE_CODE},
                     l.${GE_Custom_Form_LocalDao.SITE_DESC},
                     d.${GE_Custom_Form_DataDao.SO_PREFIX},
                     d.${GE_Custom_Form_DataDao.SO_CODE},
                     d.${GE_Custom_Form_DataDao.DATE_END}                     
                    FROM 
                     ${GE_Custom_Form_LocalDao.TABLE} l,
                     ${GE_Custom_Form_DataDao.TABLE} d
                    WHERE
                     l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = d.${GE_Custom_Form_DataDao.CUSTOMER_CODE}
                     and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_TYPE}
                     and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_CODE}
                     and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_VERSION}
                     and l.${GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA} = d.${GE_Custom_Form_DataDao.CUSTOM_FORM_DATA}
                     --
                     and l.${GE_Custom_Form_LocalDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter
                     and ($tagOperCode is null or l.${GE_Custom_Form_LocalDao.TAG_OPERATIONAL_CODE} = $tagOperCode) 
                     and ($productCode is null or l.${GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE} = $productCode )
                     and ($serialId is null or l.${GE_Custom_Form_LocalDao.SERIAL_ID} = $serialId )                    
                     and ($calendarDate is null or strftime('%Y-%m-%d',d.${GE_Custom_Form_DataDao.DATE_START},'$deviceGMT') = $calendarDate )                 
              """
        return s
    }
}