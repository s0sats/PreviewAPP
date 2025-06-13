package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class SqlAct083_001(
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
        private val userFocus: Int,
        private var isSerialSiteMode: Int = 0,
        private val multStepsLbl: String?
) : Specification {
    private val deviceGMT = ToolBox.getDeviceGMT(false)
    private var periodDateFilter: String = ""
    private var statusFilter: String =""

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
    }

    private fun getPeriodFilter(): String {
        return if (!ToolBox_Inf.hasSoOrIOProfile(context)) {
            when (ToolBox_Inf.getActionTimeDefaultOption(context)) {
                ConstantBaseApp.PREFERENCE_HOME_UNTIL_TODAY_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "'))"
                ConstantBaseApp.PREFERENCE_HOME_NEXT_WEEK_OPTION -> "  and   (strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') <= strftime('%Y-%m-%d','now','" + deviceGMT + "','+7 days'))"
                else -> ""
            }
        }else{
            ""
        }
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
        if(isSerialSiteMode == 0) {
            siteCode = null
        }
        productCode = null
        serialId = null
        calendarDate = null
        getStatusFilter()
    }

    private fun getStatusFilter() {
        statusFilter = when (userFocus) {
            1 -> """    and c.${TkTicketCacheDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}') """
            else -> """    and c.${TkTicketCacheDao.TICKET_STATUS}  = '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}'"""
        }
    }

    override fun toSqlQuery(): String {
        var s = """ SELECT
                     c.customer_code,
                     c.ticket_prefix,
                     c.ticket_code,
                     c.scn,
                     c.user_level_min,
                     c.ticket_id,
                     c.ticket_edi_id,
                     c.tag_operational_code,
                     c.tag_operational_id,
                     c.tag_operational_desc,
                     c.type_code,
                     c.type_id,
                     c.type_desc,
                     c.user_focus,
                     c.main_user,
                     c.order_by,
                     c.client_code,
                     c.client_id,
                     c.client_name,
                     c.contract_code,
                     c.contract_id,
                     c.contract_desc,
                     c.open_site_code,
                     c.open_site_desc,
                     c.open_zone_code,
                     c.open_zone_desc,
                     c.open_operation_code,
                     c.open_operation_desc,
                     c.open_product_code,
                     c.open_product_desc,
                     c.open_serial_id,
                     c.current_step_order,
                     c.ticket_status,
                     c.origin_type,
                     c.origin_desc,
                     c.internal_comments,
                     case when c.step_desc is null then '${multStepsLbl}' else  c.step_desc end step_desc,
                     c.forecast_start,
                     c.forecast_end,
                     c.step_count,
                     c.step_order_seq,
                     c.main_user,
                     c.class_id,
                     c.class_color,
                     c.kanban,
                     c.kanban_stage,
                     c.able_to_done,
                     c.preventive,
                     c.is_priority
                    FROM
                     ${TkTicketCacheDao.TABLE} c 
                    WHERE
                     c.${TkTicketCacheDao.CUSTOMER_CODE} = $customerCode
                     $statusFilter
                     and ($userFocus is null or c.${TkTicketCacheDao.USER_FOCUS} = $userFocus)                     
                     and ($tagOperCode is null or c.${TkTicketCacheDao.TAG_OPERATIONAL_CODE} = $tagOperCode)
                     and ($siteCode is null or c.${TkTicketCacheDao.OPEN_SITE_CODE}  = $siteCode)
                     and ($productCode is null or c.${TkTicketCacheDao.OPEN_PRODUCT_CODE} = $productCode )
                     and ('$serialId' is null or c.${TkTicketCacheDao.OPEN_SERIAL_ID} = '$serialId' )
                     and ('$clientId' is null or c.${TkTicketCacheDao.CLIENT_ID} = '$clientId')
                     and ('$contractId' is null or c.${TkTicketCacheDao.CONTRACT_ID} = '$contractId')
                     and ('$ticketId' is null or c.${TkTicketCacheDao.TICKET_ID} = '$ticketId')
                     and ('$calendarDate' is null or strftime('%Y-%m-%d',c.${TkTicketCacheDao.FORECAST_START},'$deviceGMT') = '$calendarDate')
                     $periodDateFilter
                     and NOT EXISTS(SELECT 1
                                    FROM ${TK_TicketDao.TABLE} t
                                    WHERE t.${TK_TicketDao.CUSTOMER_CODE} = c.${TkTicketCacheDao.CUSTOMER_CODE}
                                          and t.${TK_TicketDao.TICKET_PREFIX} = c.${TkTicketCacheDao.TICKET_PREFIX}
                                          and t.${TK_TicketDao.TICKET_CODE} = c.${TkTicketCacheDao.TICKET_CODE}
                                          )
              """.replace("'null'","null")
        return s
    }
}