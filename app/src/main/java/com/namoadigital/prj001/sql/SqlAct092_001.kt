package com.namoadigital.prj001.sql

import android.content.Context
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.TK_TicketDao
import com.namoadigital.prj001.dao.TkTicketCacheDao
import com.namoadigital.prj001.database.Specification
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class SqlAct092_001 (
    private val context: Context,
    private val customerCode: Int,
    private var productCode: Int?,
    private var serialId: String?,
    private val multStepsLbl: String?,
    private val mainUser: Boolean = false
) : Specification {
    var mainUserFilter = ""

    init {
        if(mainUser) {
            mainUserFilter = "and c.${TkTicketCacheDao.MAIN_USER} = ${ToolBox_Con.getPreference_User_Code(context)}"
        }
    }

    override fun toSqlQuery(): String {
        val s = """ SELECT
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
                     $mainUserFilter
                     and c.${TkTicketCacheDao.TICKET_STATUS} in('${ConstantBaseApp.SYS_STATUS_PENDING}','${ConstantBaseApp.SYS_STATUS_PROCESS}', '${ConstantBaseApp.SYS_STATUS_WAITING_SYNC}')                      
                     and ($productCode is null or c.${TkTicketCacheDao.OPEN_PRODUCT_CODE} = $productCode )
                     and ('$serialId' is null or c.${TkTicketCacheDao.OPEN_SERIAL_ID} = '$serialId')                     
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